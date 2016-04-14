/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:TablesNamesFinder.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Jan 22, 2013 3:31:06 PM
 * 
 */
package com.pixshow.framework.ddb.internal;

import java.io.StringReader;

import com.pixshow.framework.ddb.api.DDBCentext;
import com.pixshow.framework.ddb.api.DDBRoutingRuleAdapter;
import com.pixshow.framework.ddb.api.Sharding;
import com.pixshow.framework.log.api.SysLog;
import com.pixshow.framework.log.api.SysLogFactory;
import com.pixshow.framework.plugin.api.PluginRegisterManager;
import com.pixshow.framework.utils.StringUtility;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.util.deparser.StatementDeParser;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since Jan 22, 2013
 * 
 */

public class SqlConverter {

    private static SysLog              log = SysLogFactory.getLog(SqlConverter.class);
    private static CCJSqlParserManager pm  = new CCJSqlParserManager();

    public static String sql(String sql, DDBCentext centext) {

        if (centext == null) {
            return sql;
        }

        Sharding sharding = centext.getSharding();
        DDBRoutingRuleAdapter adapter = null;
        if (StringUtility.isNotEmpty(centext.getRule())) {
            adapter = PluginRegisterManager.getInstance().getUniquePlugin(DDBRoutingRuleAdapter.class, centext.getRule());
        }

        if (adapter == null && sharding == null) {
            return sql;
        }

        String newSql = null;
        try {
            Statement statement = pm.parse(new StringReader(sql));
            if (statement instanceof Select) {
                select((Select) statement, adapter, sharding);
            } else if (statement instanceof Insert) {
                insert((Insert) statement, adapter, sharding);
            } else if (statement instanceof Delete) {
                delete((Delete) statement, adapter, sharding);
            } else if (statement instanceof Update) {
                update((Update) statement, adapter, sharding);
            }
            StatementDeParser deParser = new StatementDeParser(new StringBuffer());
            statement.accept(deParser);
            newSql = deParser.getBuffer().toString();
        } catch (JSQLParserException e) {
            newSql = sql;
        }

        if (log.isDebugEnabled()) {
            log.debug("Sql Converter : " + sql + " -> " + newSql);
        }

        return newSql;
    }

    private static void select(Select select, DDBRoutingRuleAdapter adapter, Sharding sharding) {
        select.getSelectBody().accept(new SelectSqlConverter(adapter, sharding));
    }

    private static void delete(Delete delete, DDBRoutingRuleAdapter adapter, Sharding sharding) {
        Table table = delete.getTable();
        if (StringUtility.isNotEmpty(sharding.getSchema())) {
            table.setSchemaName(sharding.getSchema());
        }
        if (adapter != null) {
            table.setName(adapter.getTableName(table.getName(), sharding));
        }
    }

    private static void insert(Insert insert, DDBRoutingRuleAdapter adapter, Sharding sharding) {
        Table table = insert.getTable();
        if (StringUtility.isNotEmpty(sharding.getSchema())) {
            table.setSchemaName(sharding.getSchema());
        }
        if (adapter != null) {
            table.setName(adapter.getTableName(table.getName(), sharding));
        }
    }

    private static void update(Update update, DDBRoutingRuleAdapter adapter, Sharding sharding) {
        Table table = update.getTable();
        if (StringUtility.isNotEmpty(sharding.getSchema())) {
            table.setSchemaName(sharding.getSchema());
        }
        if (adapter != null) {
            table.setName(adapter.getTableName(table.getName(), sharding));
        }
    }
}
