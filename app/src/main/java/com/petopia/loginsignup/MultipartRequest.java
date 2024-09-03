package com.petopia.loginsignup;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

public class MultipartRequest extends Request<String> {
    private final String boundary = "*****";
    private final String twoHyphens = "--";
    private final String lineEnd = "\r\n";
    private Map<String, String> params;
    private Map<String, DataPart> dataParams;
    private Response.Listener<String> mListener;
    private DataOutputStream dos;

    public MultipartRequest(String url, Response.ErrorListener errorListener,
                            Response.Listener<String> listener, Map<String, String> params,
                            Map<String, DataPart> dataParams) {
        super(Method.POST, url, errorListener);
        this.params = params;
        this.dataParams = dataParams;
        mListener = listener;
    }

    @Override
    public String getBodyContentType() {
        return "multipart/form-data; boundary=" + boundary;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        dos = new DataOutputStream(bos);

        try {
            // Add parameters
            if (params != null && !params.isEmpty()) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    buildTextPart(entry.getKey(), entry.getValue());
                }
            }

            // Add data parts
            if (dataParams != null && !dataParams.isEmpty()) {
                for (Map.Entry<String, DataPart> entry : dataParams.entrySet()) {
                    buildDataPart(entry.getKey(), entry.getValue());
                }
            }

            // Close multipart form data after text and file data
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        try {
            String data = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(data, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new VolleyError(e));
        }
    }

    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
    }

    private void buildTextPart(String key, String value) throws IOException {
        dos.writeBytes(twoHyphens + boundary + lineEnd);
        dos.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"" + lineEnd);
        dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
        dos.writeBytes(lineEnd);
        dos.writeBytes(value + lineEnd);
    }

    private void buildDataPart(String key, DataPart dataPart) throws IOException {
        dos.writeBytes(twoHyphens + boundary + lineEnd);
        dos.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"; filename=\""
                + dataPart.getFileName() + "\"" + lineEnd);
        if (dataPart.getType() != null && !dataPart.getType().trim().isEmpty()) {
            dos.writeBytes("Content-Type: " + dataPart.getType() + lineEnd);
        }
        dos.writeBytes(lineEnd);
        dos.write(dataPart.getData());
        dos.writeBytes(lineEnd);
    }
}
