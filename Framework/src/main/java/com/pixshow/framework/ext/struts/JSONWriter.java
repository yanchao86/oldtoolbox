/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:JSONWriter.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Jan 18, 2013 3:27:18 PM
 * 
 */
package com.pixshow.framework.ext.struts;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.text.CharacterIterator;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.StringCharacterIterator;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Pattern;

import net.sf.json.JSONNull;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.json.JSONException;
import org.apache.struts2.json.annotations.JSON;
import org.apache.struts2.json.annotations.JSONFieldBridge;
import org.apache.struts2.json.annotations.JSONParameter;
import org.apache.struts2.json.bridge.FieldBridge;
import org.apache.struts2.json.bridge.ParameterizedBridge;

import com.pixshow.framework.abbr.internal.AbbreviationProcessor;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since Jan 18, 2013
 * 
 */
/**
 * <p>
 * Serializes an object into JavaScript Object Notation (JSON). If cyclic
 * references are detected they will be nulled out.
 * </p>
 */
class JSONWriter {

    protected final static String RFC3339_FORMAT       = "yyyy-MM-dd'T'HH:mm:ss";
    protected final static String DEFAULT_FORMAT       = "yyyy-MM-dd HH:mm:ss";

    private static final Log      LOG                  = LogFactory.getLog(JSONWriter.class);

    /**
     * By default, enums are serialzied as name=value pairs
     */
    public static final boolean   ENUM_AS_BEAN_DEFAULT = false;

    static char[]                 hex                  = "0123456789ABCDEF".toCharArray();
    private boolean               enableAbbr           = false;
    private boolean               excludeEmpty         = false;

    private StringBuilder         buf                  = new StringBuilder();
    private Stack<Object>         stack                = new Stack<Object>();
    private boolean               ignoreHierarchy      = true;
    private Object                root;
    private boolean               buildExpr            = true;
    private String                exprStack            = "";
    private Collection<Pattern>   excludeProperties;
    private Collection<Pattern>   includeProperties;
    private DateFormat            formatter;
    private boolean               enumAsBean           = ENUM_AS_BEAN_DEFAULT;
    private boolean               excludeNullProperties;

    /**
     * @param object Object to be serialized into JSON
     * @return JSON string for object
     * @throws JSONException
     */
    public String write(Object object) throws JSONException {
        return this.write(object, null, null, false);
    }

    /**
     * @param object Object to be serialized into JSON
     * @return JSON string for object
     * @throws JSONException
     */
    public String write(Object object, Collection<Pattern> excludeProperties, Collection<Pattern> includeProperties, boolean excludeNullProperties) throws JSONException {
        this.excludeNullProperties = excludeNullProperties;
        this.buf.setLength(0);
        this.root = object;
        this.exprStack = "";
        this.buildExpr = ((excludeProperties != null) && !excludeProperties.isEmpty()) || ((includeProperties != null) && !includeProperties.isEmpty());
        this.excludeProperties = excludeProperties;
        this.includeProperties = includeProperties;
        this.value(object, null);

        return this.buf.toString();
    }

    /**
     * Detect cyclic references
     */
    private void value(Object object, Method method) throws JSONException {
        if (object == null || object instanceof JSONNull) {
            this.add("null");
            return;
        }

        if (this.stack.contains(object)) {
            Class clazz = object.getClass();

            // cyclic reference
            if (clazz.isPrimitive() || clazz.equals(String.class)) {
                this.process(object, method);
            } else {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Cyclic reference detected on " + object);
                }

                this.add("null");
            }

            return;
        }

        this.process(object, method);
    }

    /**
     * Serialize object into json
     */
    private void process(Object object, Method method) throws JSONException {
        this.stack.push(object);

        if (object instanceof Class) {
            this.string(object);
        } else if (object instanceof Boolean) {
            this.bool((Boolean) object);
        } else if (object instanceof Number) {
            this.add(object);
        } else if (object instanceof String) {
            this.string(object);
        } else if (object instanceof Character) {
            this.string(object);
        } else if (object instanceof Map) {
            this.map((Map) object, method);
        } else if (object.getClass().isArray()) {
            this.array(object, method);
        } else if (object instanceof Iterable) {
            this.array(((Iterable) object).iterator(), method);
        } else if (object instanceof Date) {
            this.date((Date) object, method);
        } else if (object instanceof Calendar) {
            this.date(((Calendar) object).getTime(), method);
        } else if (object instanceof Locale) {
            this.string(object);
        } else if (object instanceof Enum) {
            this.enumeration((Enum) object);
        } else {
            this.bean(object);
        }

        this.stack.pop();
    }

    /**
     * Instrospect bean and serialize its properties
     */
    private void bean(Object object) throws JSONException {
        this.add("{");

        BeanInfo info;

        try {
            Class clazz = object.getClass();

            info = ((object == this.root) && this.ignoreHierarchy) ? Introspector.getBeanInfo(clazz, clazz.getSuperclass()) : Introspector.getBeanInfo(clazz);

            PropertyDescriptor[] props = info.getPropertyDescriptors();

            boolean hasData = false;
            for (PropertyDescriptor prop : props) {
                String name = prop.getName();
                Method accessor = prop.getReadMethod();
                Method baseAccessor = findBaseAccessor(clazz, accessor);

                if (baseAccessor != null) {
                    if (baseAccessor.isAnnotationPresent(JSON.class)) {
                        JSONAnnotationFinder jsonFinder = new JSONAnnotationFinder(baseAccessor).invoke();

                        if (!jsonFinder.shouldSerialize())
                            continue;
                        if (jsonFinder.getName() != null) {
                            name = jsonFinder.getName();
                        }
                    }
                    // ignore "class" and others
                    if (this.shouldExcludeProperty(prop)) {
                        continue;
                    }
                    String expr = null;
                    if (this.buildExpr) {
                        expr = this.expandExpr(name);
                        if (this.shouldExcludeProperty(expr)) {
                            continue;
                        }
                        expr = this.setExprStack(expr);
                    }

                    Object value = accessor.invoke(object);
                    if (baseAccessor.isAnnotationPresent(JSONFieldBridge.class)) {
                        value = getBridgedValue(baseAccessor, value);
                    }

                    boolean propertyPrinted = this.add(name, value, accessor, hasData);
                    hasData = hasData || propertyPrinted;
                    if (this.buildExpr) {
                        this.setExprStack(expr);
                    }
                }
            }

            // special-case handling for an Enumeration - include the name() as
            // a property */
            if (object instanceof Enum) {
                Object value = ((Enum) object).name();
                this.add("_name", value, object.getClass().getMethod("name"), hasData);
            }
        } catch (Exception e) {
            throw new JSONException(e);
        }

        this.add("}");
    }

    private Object getBridgedValue(Method baseAccessor, Object value) throws InstantiationException, IllegalAccessException {
        JSONFieldBridge fieldBridgeAnn = baseAccessor.getAnnotation(JSONFieldBridge.class);
        if (fieldBridgeAnn != null) {
            Class impl = fieldBridgeAnn.impl();
            FieldBridge instance = (FieldBridge) impl.newInstance();

            if (fieldBridgeAnn.params().length > 0 && ParameterizedBridge.class.isAssignableFrom(impl)) {
                Map<String, String> params = new HashMap<String, String>(fieldBridgeAnn.params().length);
                for (JSONParameter param : fieldBridgeAnn.params()) {
                    params.put(param.name(), param.value());
                }
                ((ParameterizedBridge) instance).setParameterValues(params);
            }
            value = instance.objectToString(value);
        }
        return value;
    }

    private Method findBaseAccessor(Class clazz, Method accessor) {
        Method baseAccessor = null;
        if (clazz.getName().indexOf("$$EnhancerByCGLIB$$") > -1) {
            try {
                baseAccessor = Thread.currentThread().getContextClassLoader().loadClass(clazz.getName().substring(0, clazz.getName().indexOf("$$"))).getMethod(accessor.getName(), accessor.getParameterTypes());
            } catch (Exception ex) {
                LOG.debug(ex.getMessage(), ex);
            }
        } else if (clazz.getName().indexOf("$$_javassist") > -1) {
            try {
                baseAccessor = Class.forName(clazz.getName().substring(0, clazz.getName().indexOf("_$$"))).getMethod(accessor.getName(), accessor.getParameterTypes());
            } catch (Exception ex) {
                LOG.debug(ex.getMessage(), ex);
            }
        } else {
            return accessor;
        }
        return baseAccessor;
    }

    /**
     * Instrospect an Enum and serialize it as a name/value pair or as a bean
     * including all its own properties
     */
    private void enumeration(Enum enumeration) throws JSONException {
        if (enumAsBean) {
            this.bean(enumeration);
        } else {
            this.string(enumeration.name());
        }
    }

    private boolean shouldExcludeProperty(PropertyDescriptor prop) throws SecurityException, NoSuchFieldException {
        String name = prop.getName();

        return name.equals("class") || name.equals("declaringClass") || name.equals("cachedSuperClass") || name.equals("metaClass");

    }

    private String expandExpr(int i) {
        return this.exprStack + "[" + i + "]";
    }

    private String expandExpr(String property) {
        if (this.exprStack.length() == 0)
            return property;
        return this.exprStack + "." + property;
    }

    private String setExprStack(String expr) {
        String s = this.exprStack;
        this.exprStack = expr;
        return s;
    }

    private boolean shouldExcludeProperty(String expr) {
        if (this.excludeProperties != null) {
            for (Pattern pattern : this.excludeProperties) {
                if (pattern.matcher(expr).matches()) {
                    if (LOG.isDebugEnabled())
                        LOG.debug("Ignoring property because of exclude rule: " + expr);
                    return true;
                }
            }
        }

        if (this.includeProperties != null) {
            for (Pattern pattern : this.includeProperties) {
                if (pattern.matcher(expr).matches()) {
                    return false;
                }
            }

            if (LOG.isDebugEnabled())
                LOG.debug("Ignoring property because of include rule:  " + expr);
            return true;
        }

        return false;
    }

    public void setEnableAbbr(boolean enable) {
        this.enableAbbr = enable;
    }

    public boolean isAbbr() {
        return this.enableAbbr;
    }

    public boolean isExcludeEmpty() {
        return excludeEmpty;
    }

    public void setExcludeEmpty(boolean excludeEmpty) {
        this.excludeEmpty = excludeEmpty;
    }

    /**
     * Add name/value pair to buffer
     */
    private boolean add(String name, Object value, Method method, boolean hasData) throws JSONException {
        if (excludeNullProperties && value == null) {
            return false;
        }

        if (isAbbr() && isEmpty(value)) {
            return false;
        }

        if (hasData) {
            this.add(',');
        }
        this.add('"');
        this.add(isAbbr() ? AbbreviationProcessor.abbr(name) : name);
        this.add("\":");
        this.value(value, method);
        return true;
    }

    /**
     * Add map to buffer
     */
    private void map(Map map, Method method) throws JSONException {
        this.add("{");

        Iterator it = map.entrySet().iterator();

        boolean warnedNonString = false; // one report per map
        boolean hasData = false;
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            if (excludeNullProperties && entry.getValue() == null) {
                continue;
            }

            Object key = entry.getKey();
            Object value = entry.getValue();
            if (isAbbr() && isEmpty(value)) {
                continue;
            }

            String expr = null;
            if (this.buildExpr) {
                if (key == null) {
                    LOG.error("Cannot build expression for null key in " + this.exprStack);
                    continue;
                } else {
                    expr = this.expandExpr(key.toString());
                    if (this.shouldExcludeProperty(expr)) {
                        continue;
                    }
                    expr = this.setExprStack(expr);
                }
            }
            if (hasData) {
                this.add(',');
            }
            hasData = true;
            if (!warnedNonString && !(key instanceof String)) {
                if (LOG.isWarnEnabled()) {
                    LOG.warn("JavaScript doesn't support non-String keys, using toString() on " + key.getClass().getName());
                }
                warnedNonString = true;
            }
            this.value(isAbbr() ? AbbreviationProcessor.abbr(key.toString()) : key.toString(), method);
            this.add(":");
            this.value(entry.getValue(), method);
            if (this.buildExpr) {
                this.setExprStack(expr);
            }
        }

        this.add("}");
    }

    /**
     * Add date to buffer
     */
    private void date(Date date, Method method) {
        JSON json = null;
        if (method != null)
            json = method.getAnnotation(JSON.class);
        if (this.formatter == null)
            this.formatter = new SimpleDateFormat(DEFAULT_FORMAT);

        DateFormat formatter = (json != null) && (json.format().length() > 0) ? new SimpleDateFormat(json.format()) : this.formatter;
        this.string(formatter.format(date));
    }

    /**
     * Add array to buffer
     */
    private void array(Iterator it, Method method) throws JSONException {
        this.add("[");

        boolean hasData = false;
        for (int i = 0; it.hasNext(); i++) {
            String expr = null;
            if (this.buildExpr) {
                expr = this.expandExpr(i);
                if (this.shouldExcludeProperty(expr)) {
                    it.next();
                    continue;
                }
                expr = this.setExprStack(expr);
            }
            if (hasData) {
                this.add(',');
            }
            hasData = true;
            this.value(it.next(), method);
            if (this.buildExpr) {
                this.setExprStack(expr);
            }
        }

        this.add("]");
    }

    /**
     * Add array to buffer
     */
    private void array(Object object, Method method) throws JSONException {
        this.add("[");

        int length = Array.getLength(object);

        boolean hasData = false;
        for (int i = 0; i < length; ++i) {
            String expr = null;
            if (this.buildExpr) {
                expr = this.expandExpr(i);
                if (this.shouldExcludeProperty(expr)) {
                    continue;
                }
                expr = this.setExprStack(expr);
            }
            if (hasData) {
                this.add(',');
            }
            hasData = true;
            this.value(Array.get(object, i), method);
            if (this.buildExpr) {
                this.setExprStack(expr);
            }
        }

        this.add("]");
    }

    /**
     * Add boolean to buffer
     */
    private void bool(boolean b) {
        this.add(b ? "true" : "false");
    }

    /**
     * escape characters
     */
    private void string(Object obj) {
        this.add('"');

        CharacterIterator it = new StringCharacterIterator(obj.toString());

        for (char c = it.first(); c != CharacterIterator.DONE; c = it.next()) {
            if (c == '"') {
                this.add("\\\"");
            } else if (c == '\\') {
                this.add("\\\\");
            } else if (c == '/') {
                this.add("\\/");
            } else if (c == '\b') {
                this.add("\\b");
            } else if (c == '\f') {
                this.add("\\f");
            } else if (c == '\n') {
                this.add("\\n");
            } else if (c == '\r') {
                this.add("\\r");
            } else if (c == '\t') {
                this.add("\\t");
            } else if (Character.isISOControl(c)) {
                this.unicode(c);
            } else {
                this.add(c);
            }
        }

        this.add('"');
    }

    /**
     * Add object to buffer
     */
    private void add(Object obj) {
        this.buf.append(obj);
    }

    /**
     * Add char to buffer
     */
    private void add(char c) {
        this.buf.append(c);
    }

    /**
     * Represent as unicode
     *
     * @param c character to be encoded
     */
    private void unicode(char c) {
        this.add("\\u");

        int n = c;

        for (int i = 0; i < 4; ++i) {
            int digit = (n & 0xf000) >> 12;

            this.add(hex[digit]);
            n <<= 4;
        }
    }

    public void setIgnoreHierarchy(boolean ignoreHierarchy) {
        this.ignoreHierarchy = ignoreHierarchy;
    }

    /**
     * If true, an Enum is serialized as a bean with a special property
     * _name=name() as all as all other properties defined within the enum.<br/>
     * If false, an Enum is serialized as a name=value pair (name=name())
     *
     * @param enumAsBean true to serialize an enum as a bean instead of as a name=value
     *                   pair (default=false)
     */
    public void setEnumAsBean(boolean enumAsBean) {
        this.enumAsBean = enumAsBean;
    }

    private static class JSONAnnotationFinder {
        private boolean serialize = true;
        private Method  accessor;
        private String  name;

        public JSONAnnotationFinder(Method accessor) {
            this.accessor = accessor;
        }

        boolean shouldSerialize() {
            return serialize;
        }

        public String getName() {
            return name;
        }

        public JSONAnnotationFinder invoke() {
            JSON json = accessor.getAnnotation(JSON.class);
            serialize = json.serialize();
            if (serialize && json.name().length() > 0) {
                name = json.name();
            }
            return this;
        }
    }

    private boolean isEmpty(Object value) {
        if (isExcludeEmpty() && (value == null //
                || (value instanceof Integer && ((Integer) value) == 0)//
                || (value instanceof Long && ((Long) value) == 0)// 
                || (value instanceof Double && ((Double) value) == 0)// 
                || (value instanceof Float && ((Float) value) == 0)// 
                || (value instanceof Boolean && !((Boolean) value))//
        || (value instanceof String && ((String) value).isEmpty()))) {
            return true;
        }
        return false;
    }
}
