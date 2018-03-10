package com.demo.utils;

import java.io.IOException;

public interface InputStreamCallback {

	/**
	 * 
	 * @param blockArray
	 *            讀取到的byte[]
	 * @return 是否繼續讀取下一段byte[],
	 * 
	 *         true=繼續往下一段byte[], false=中斷
	 * @throws IOException
	 */
	boolean doInAction(byte[] blockArray);
}
