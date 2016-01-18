package com.pixshow.toolboxmgr.servlet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pixshow.framework.utils.AppContextUtility;
import com.pixshow.framework.utils.CipherUtility;
import com.pixshow.toolboxmgr.service.StatService;

public class BwkgServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private StatService       statService      = AppContextUtility.getContext().getBean(StatService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String contentEncoding = request.getHeader("Content-Encoding");
        ServletInputStream servletIn = request.getInputStream();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int len = 0;
        while ((len = servletIn.read(buf)) != -1) {
            out.write(buf, 0, len);
        }
        byte[] rawData = out.toByteArray();
        byte[] decryptData = CipherUtility.AES.decrypt(rawData, "dsjkfh824hnlkdfnmvo");
        byte[] data = "gzip".equalsIgnoreCase(contentEncoding) ? ungzip(decryptData) : decryptData;
        String result = new String(data, "UTF-8");

        statService.bkw(result);
        response.getWriter().print("{\"result\": 0}");
    }

    private static byte[] ungzip(byte[] data) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        GZIPInputStream gin = null;

        try {
            gin = new GZIPInputStream(in);
            byte[] buf = new byte[1024];
            int len = 0;
            while ((len = gin.read(buf)) != -1) {
                out.write(buf, 0, len);
            }
            return out.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (gin != null) try {
                gin.close();
            } catch (IOException ignore) {
            }
            if (out != null) try {
                out.close();
            } catch (IOException ignore) {
            }
        }
        return null;
    }

}
