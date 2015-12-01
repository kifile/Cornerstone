package com.kifile.android.cornerstone.impl.fetchers;

import android.util.Log;

import com.kifile.android.cornerstone.core.ConvertException;
import com.kifile.android.cornerstone.core.DataFetcher;
import com.kifile.android.cornerstone.core.FetchException;
import com.kifile.android.cornerstone.utils.HttpUtils;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * 网络库访问.
 *
 * @author kifile
 */
public class HttpFetcher implements DataFetcher<String> {

    public static final int METHOD_HEAD = 0;
    public static final int METHOD_GET = 1;
    public static final int METHOD_POST = 2;
    public static final int METHOD_DELETE = 3;
    public static final int METHOD_PUT = 4;
    public static final int METHOD_PATCH = 5;

    public static final int BODY_TYPE_FORM = 0;
    public static final int BODY_TYPE_JSON = 1;

    private final int mMethod;

    private int mBodyType;

    private final String mUrl;

    private Map<String, String> mParams;

    private Map<String, FileEntry> mFileParams;

    private Headers mHttpHeader;

    public HttpFetcher(int method, String url) {
        mMethod = method;
        mUrl = url;
    }

    public void setBodyType(int type) {
        mBodyType = type;
    }

    public void reset() {
        mBodyType = BODY_TYPE_FORM;
        mParams = null;
        mFileParams = null;
        mHttpHeader = null;
    }

    public void setParams(Map<String, String> params) {
        mParams = params;
    }

    public void setFileParams(Map<String, FileEntry> fileParams) {
        mFileParams = fileParams;
    }

    public void setHttpHeader(Map<String, String> headers) {
        if (headers != null) {
            Headers.Builder headerBuilder = new Headers.Builder();
            for (Map.Entry<String, String> header : headers.entrySet()) {
                headerBuilder.add(header.getKey(), header.getValue());
            }
            setHttpHeader(headerBuilder.build());
        }
    }

    public void setHttpHeader(Headers header) {
        mHttpHeader = header;
    }

    @Override
    public String fetch() throws FetchException, ConvertException {
        Request.Builder requestBuilder;
        switch (mMethod) {
            case METHOD_HEAD: {
                requestBuilder = new Request.Builder().url(mUrl).head();
                break;
            }
            case METHOD_GET: {
                String url = appendParams(mUrl, mParams);
                requestBuilder = new Request.Builder().url(url).get();
                break;
            }
            case METHOD_POST: {
                requestBuilder = new Request.Builder().url(mUrl).post(getRequestBody(mParams, mFileParams));
                break;
            }
            case METHOD_DELETE: {
                requestBuilder = new Request.Builder().url(mUrl).delete(getRequestBody(mParams, mFileParams));
                break;
            }
            case METHOD_PUT: {
                requestBuilder = new Request.Builder().url(mUrl).put(getRequestBody(mParams, mFileParams));
                break;
            }
            case METHOD_PATCH: {
                requestBuilder = new Request.Builder().url(mUrl).patch(getRequestBody(mParams, mFileParams));
                break;
            }
            default:
                throw new IllegalArgumentException("Unsupported method:" + mMethod);
        }
        if (requestBuilder != null) {
            try {
                if (mHttpHeader != null) {
                    requestBuilder.headers(mHttpHeader);
                }
                Response response = HttpUtils.getResponse(requestBuilder.build());
                if (response.isSuccessful()) {
                    return response.body().string();
                } else {
                    Log.e("HttpBody", response.body().string());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private String appendParams(String url, Map<String, String> params) {
        StringBuffer link = new StringBuffer(url);
        if (params != null) {
            boolean first = true;
            for (Map.Entry<String, String> param : params.entrySet()) {
                if (first) {
                    first = false;
                    link.append("?");
                } else {
                    link.append("&");
                }
                link.append(param.getKey()).append("=").append(param.getValue());
            }
        }
        return link.toString();
    }

    private RequestBody getRequestBody(Map<String, String> params, Map<String, FileEntry> fileParams) {
        switch (mBodyType) {
            case BODY_TYPE_FORM: {
                if ((params == null || params.size() == 0) && (fileParams == null || fileParams.size() == 0)) {
                    return RequestBody.create(null, new byte[0]);
                } else if (fileParams == null || fileParams.size() == 0) {
                    FormEncodingBuilder builder = new FormEncodingBuilder();
                    for (Map.Entry<String, String> entry : params.entrySet()) {
                        builder.add(entry.getKey(), entry.getValue());
                    }
                    return builder.build();
                } else {
                    MultipartBuilder builder = new MultipartBuilder();
                    if (params != null) {
                        for (Map.Entry<String, String> entry : params.entrySet()) {
                            builder.addFormDataPart(entry.getKey(), entry.getValue());
                        }
                    }
                    for (Map.Entry<String, FileEntry> fileEntry : fileParams.entrySet()) {
                        FileEntry entry = fileEntry.getValue();
                        builder.addFormDataPart(fileEntry.getKey(), entry.file.getName(),
                                RequestBody.create(MediaType.parse(entry.mime), entry.file));
                    }
                    return builder.build();
                }
            }
            case BODY_TYPE_JSON: {
                JSONObject body = new JSONObject();
                if (params != null) {
                    for (Map.Entry<String, String> entry : params.entrySet()) {
                        try {
                            body.put(entry.getKey(), entry.getValue());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                return RequestBody.create(MediaType.parse("application/json"), body.toString());
            }
            default:
                return null;
        }
    }

    public static class FileEntry {

        public File file;

        public String mime;

        private FileEntry(File file, String mime) {
            this.file = file;
            this.mime = mime;
        }

        public static FileEntry create(File file, String mime) {
            return new FileEntry(file, mime);
        }

    }
}
