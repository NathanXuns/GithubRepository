package com.android.main.lib.util;

import com.android.main.lib.interfaces.ProgressListener;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;


public class CustomMultipartEntity extends MultipartEntity {
	private final ProgressListener listener;

	public CustomMultipartEntity(final ProgressListener listener) {
		super();
		this.listener = listener;
	}

	public CustomMultipartEntity(final HttpMultipartMode mode,
			final ProgressListener listener) {
		super(mode);
		this.listener = listener;
	}

	public CustomMultipartEntity(HttpMultipartMode mode,
			final String boundary,

			final Charset charset, final ProgressListener listener) {

		super(mode, boundary, charset);

		this.listener = listener;

	}

	@Override
	public void writeTo(final OutputStream outstream) throws IOException {
		super.writeTo(new CountingOutputStream(outstream, this.listener));
	}
}
