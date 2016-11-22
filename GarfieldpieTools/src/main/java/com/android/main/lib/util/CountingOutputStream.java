package com.android.main.lib.util;

import com.android.main.lib.interfaces.ProgressListener;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;


public class CountingOutputStream extends FilterOutputStream {
	private final ProgressListener listener;

	private long transferred;

	public CountingOutputStream(final OutputStream out,
			final ProgressListener listener) {

		super(out);

		this.listener = listener;

		this.transferred = 0;

	}

	public void write(byte[] b, int off, int len) throws IOException {

		out.write(b, off, len);

		this.transferred += len;

		this.listener.transferred(this.transferred);

	}

	public void write(int b) throws IOException {

		out.write(b);

		this.transferred++;

		this.listener.transferred(this.transferred);

	}
}
