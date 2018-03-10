package com.demo.utils;

import static org.junit.Assert.*;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;
import com.mchange.io.FileUtils;

@Ignore
public class FileTypeUtilTest {

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	@Test
	public void fileType() {
		for (FileType fileType : FileType.values()) {
			System.out.println(fileType);
		}
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	@Test
	public void extensionType() {
		for (ExtensionType extensionType : ExtensionType.values()) {
			System.out.println(extensionType);
		}
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	@Test
	// round: 0.11 [+- 0.02], round.block: 0.01 [+- 0.01], round.gc: 0.00 [+-
	// 0.00], GC.calls: 2, GC.time: 0.00, time.total: 0.16, time.warmup: 0.01,
	// time.bench: 0.16
	public void getFileType() {
		String[] fileNames = new String[] {
				//
				"custom/input/file-type/Tulips.bmp",// 0
				"custom/input/file-type/Tulips.gif",// 1
				"custom/input/file-type/Tulips.jp2",// 2
				"custom/input/file-type/Tulips.jpg",// 3
				"custom/input/file-type/Tulips.pcx",// 4
				"custom/input/file-type/Tulips.png",// 5
				"custom/input/file-type/Tulips.ppm",// 6
				"custom/input/file-type/Tulips.tga",// 7 檔尾
				"custom/input/file-type/Tulips.tif",// 8
				//
				"custom/input/file-type/Resume.doc",// 9
				"custom/input/file-type/Resume.xls",// 10
				"custom/input/file-type/Resume.ppt",// 11
				"custom/input/file-type/Resume.vsd",// 12
				// "custom/input/file-type/7zSharpSetup.msi",// 13
				"custom/input/file-type/Resume.pdf",// 14
				"custom/input/file-type/Resume.eps",// 15

		};
		// 隨機檔案
		String fileName = fileNames[NumberUtil.randomInt(0, fileNames.length)];

		// 指定檔案
		// String fileName = fileNames[7];
		FileType fileType = FileTypeUtil.getFileType(fileName);
		// System.out.println(fileName + ", " + fileType);
		assertNotNull(fileType);
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	@Test
	// round: 0.52 [+- 0.21], round.block: 0.22 [+- 0.27], round.gc: 0.00 [+-
	// 0.00], GC.calls: 5, GC.time: 0.02, time.total: 0.87, time.warmup: 0.00,
	// time.bench: 0.86
	public void getFileTypeFromByteArray() throws Exception {
		String[] fileNames = new String[] {
				//
				"custom/input/file-type/Tulips.bmp",// 0
				"custom/input/file-type/Tulips.gif",// 1
				"custom/input/file-type/Tulips.jp2",// 2
				"custom/input/file-type/Tulips.jpg",// 3
				"custom/input/file-type/Tulips.pcx",// 4
				"custom/input/file-type/Tulips.png",// 5
				"custom/input/file-type/Tulips.ppm",// 6
				"custom/input/file-type/Tulips.tga",// 7 檔尾
				"custom/input/file-type/Tulips.tif",// 8
				//
				"custom/input/file-type/Resume.doc",// 9
				"custom/input/file-type/Resume.xls",// 10
				"custom/input/file-type/Resume.ppt",// 11
				"custom/input/file-type/Resume.vsd",// 12
				// "custom/input/file-type/7zSharpSetup.msi",// 13
				"custom/input/file-type/Resume.pdf",// 14
				"custom/input/file-type/Resume.eps",// 15

		};
		// 隨機檔案
		String fileName = fileNames[NumberUtil.randomInt(0, fileNames.length)];

		// 指定檔案
		// String fileName = fileNames[7];
		// 讀檔
		byte[] value = FileUtils.getBytes(new File(fileName));
		FileType fileType = FileTypeUtil.getFileType(value);
		// System.out.println(fileName + ", " + fileType);
		assertNotNull(fileType);
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	@Test
	public void getFileTypeImage() {
		String[] fileNames = new String[] {
				//
				"custom/input/file-type/Tulips.tga",// 0 檔尾
				"custom/input/file-type/Tulips.tif",// 1
		};
		// 指定檔案
		String fileName = fileNames[1];
		FileType fileType = FileTypeUtil.getFileType(fileName);
		System.out.println(fileName + ", " + fileType);
		assertNotNull(fileType);
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	@Test
	public void getFileTypeNoneImage() {
		String[] fileNames = new String[] {
				//
				"custom/input/file-type/Resume.doc",// 0
				"custom/input/file-type/Resume.xls",// 1
				"custom/input/file-type/Resume.ppt",// 2
				"custom/input/file-type/Resume.vsd",// 3
				"custom/input/file-type/7zSharpSetup.msi",// 4
				"custom/input/file-type/Resume.pdf",// 5

		};
		// 指定檔案
		String fileName = fileNames[5];
		FileType fileType = FileTypeUtil.getFileType(fileName);
		System.out.println(fileName + ", " + fileType);
		assertNotNull(fileType);
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	@Test
	public void getFileTypeXls() throws Exception {
		String[] fileNames = new String[] {
		//
		"custom/input/file-type/Resume.xls",// 0

		};
		// 指定檔案
		String fileName = fileNames[0];
		// InputStream in = new
		// FileInputStream(fileName);//java.io.FileInputStream mark/reset not
		// supported
		InputStream in = new BufferedInputStream(new FileInputStream(fileName));
		FileType fileType = FileTypeUtil.getFileType(in);
		System.out.println(fileName + ", " + fileType);
		assertNotNull(fileType);
		//
		// System.out.println("available: " + in.available());
		byte[] byteArray = new byte[8];
		in.read(byteArray);
		// 若無reset位置會改變
		// SystemUtil.println(EncodingUtil.encodeHex(byteArray));//
		// 6732cd07c1800100

		// FileTypeUtil.getFileType最後需要reset
		SystemUtil.println(EncodingUtil.encodeHex(byteArray));// d0cf11e0a1b11ae1
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	@Test
	// round: 0.10 [+- 0.01], round.block: 0.01 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 3, GC.time: 0.01, time.total: 0.11, time.warmup: 0.00,
	// time.bench: 0.11
	public void getExtensionType() {
		String[] fileNames = new String[] {
				//
				"custom/input/file-type/Tulips.bmp",// 0
				"custom/input/file-type/Tulips.gif",// 1
				"custom/input/file-type/Tulips.jp2",// 2
				"custom/input/file-type/Tulips.jpg",// 3
				"custom/input/file-type/Tulips.pcx",// 4
				"custom/input/file-type/Tulips.png",// 5
				"custom/input/file-type/Tulips.ppm",// 6
				"custom/input/file-type/Tulips.tga",// 7 檔尾
				"custom/input/file-type/Tulips.tif",// 8
				//
				"custom/input/file-type/Resume.doc",// 9
				"custom/input/file-type/Resume.xls",// 10
				"custom/input/file-type/Resume.ppt",// 11
				"custom/input/file-type/Resume.vsd",// 12
				// "custom/input/file-type/7zSharpSetup.msi",// 13
				"custom/input/file-type/Resume.pdf",// 14
				"custom/input/file-type/Resume.eps",// 15

		};
		// 隨機檔案
		String fileName = fileNames[NumberUtil.randomInt(0, fileNames.length)];

		// 指定檔案
		// String fileName = fileNames[4];
		ExtensionType extensionType = FileTypeUtil.getExtensionType(fileName);
		// System.out.println(fileName + ", " + extensionType);
		assertNotNull(extensionType);
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	@Test
	// round: 0.69 [+- 0.26], round.block: 0.27 [+- 0.27], round.gc: 0.00 [+-
	// 0.00], GC.calls: 5, GC.time: 0.02, time.total: 1.00, time.warmup: 0.01,
	// time.bench: 0.99
	public void getExtensionTypeFromByteArray() throws Exception {
		String[] fileNames = new String[] {
				//
				"custom/input/file-type/Tulips.bmp",// 0
				"custom/input/file-type/Tulips.gif",// 1
				"custom/input/file-type/Tulips.jp2",// 2
				"custom/input/file-type/Tulips.jpg",// 3
				"custom/input/file-type/Tulips.pcx",// 4
				"custom/input/file-type/Tulips.png",// 5
				"custom/input/file-type/Tulips.ppm",// 6
				"custom/input/file-type/Tulips.tga",// 7 檔尾
				"custom/input/file-type/Tulips.tif",// 8
				//
				"custom/input/file-type/Resume.doc",// 9
				"custom/input/file-type/Resume.xls",// 10
				"custom/input/file-type/Resume.ppt",// 11
				"custom/input/file-type/Resume.vsd",// 12
				// "custom/input/file-type/7zSharpSetup.msi",// 13
				"custom/input/file-type/Resume.pdf",// 14

		};
		// 隨機檔案
		String fileName = fileNames[NumberUtil.randomInt(0, fileNames.length)];

		// 指定檔案
		// String fileName = fileNames[4];
		// 讀檔
		byte[] value = FileUtils.getBytes(new File(fileName));
		ExtensionType extensionType = FileTypeUtil.getExtensionType(value);
		// System.out.println(fileName + ", " + extensionType);
		assertNotNull(extensionType);
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	@Test
	// round: 0.23 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 0, GC.time: 0.00, time.total: 0.23, time.warmup: 0.00,
	// time.bench: 0.23
	public void getExtensionTypeXls() throws Exception {
		String[] fileNames = new String[] {
		//
		"custom/input/file-type/Resume.xls",// 0

		};
		// 指定檔案
		String fileName = fileNames[0];
		// InputStream in = new
		// FileInputStream(fileName);//java.io.FileInputStream mark/reset not
		// supported

		// 若是FileInputStream, 需要用BufferedInputStream包起來,才有支援mark/reset
		InputStream in = new BufferedInputStream(new FileInputStream(fileName));
		ExtensionType extensionType = FileTypeUtil.getExtensionType(in);
		System.out.println(fileName + ", " + extensionType);
		assertNotNull(extensionType);
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	@Test
	public void searchOffsetZero() throws Exception {
		String[] fileNames = new String[] {
				//
				"custom/input/file-type/Tulips.bmp",// 0
				"custom/input/file-type/Tulips.gif",// 1
				"custom/input/file-type/Tulips.jp2",// 2
				//
				"custom/input/file-type/Resume.doc",// 3
				"custom/input/file-type/Resume.xls",// 4
				"custom/input/file-type/Resume.ppt",// 5
				"custom/input/file-type/Resume.vsd",// 6
				"custom/input/file-type/Resume.pdf",// 7
		};
		// 指定檔案
		String fileName = fileNames[0];
		FileType fileType = FileTypeUtil
				.searchOffsetZero(new BufferedInputStream(new FileInputStream(
						fileName)));
		System.out.println(fileName + ", " + fileType);
		assertNotNull(fileType);
		//

	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	@Test
	public void searchOffsetZeroByteArray() throws Exception {
		String[] fileNames = new String[] {
				//
				"custom/input/file-type/Tulips.bmp",// 0
				"custom/input/file-type/Tulips.gif",// 1
				"custom/input/file-type/Tulips.jp2",// 2
				//
				"custom/input/file-type/Resume.doc",// 3
				"custom/input/file-type/Resume.xls",// 4
				"custom/input/file-type/Resume.ppt",// 5
				"custom/input/file-type/Resume.vsd",// 6
				"custom/input/file-type/Resume.pdf",// 7
		};
		// 指定檔案
		String fileName = fileNames[1];
		// 讀檔
		byte[] value = FileUtils.getBytes(new File(fileName));
		FileType fileType = FileTypeUtil.searchOffsetZero(value);
		System.out.println(fileName + ", " + fileType);
		assertNotNull(fileType);
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	@Test
	public void searchOffsetGTZero() throws Exception {
		String[] fileNames = new String[] {
				"custom/input/file-type/Resume.doc",// 0
				"custom/input/file-type/Resume.xls",// 1
				"custom/input/file-type/Resume.ppt",// 2
				"custom/input/file-type/Resume.vsd",// 3
		};
		// 指定檔案
		String fileName = fileNames[0];
		FileType fileType = FileTypeUtil
				.searchOffsetGTZero(new BufferedInputStream(
						new FileInputStream(fileName)));
		System.out.println(fileName + ", " + fileType);
		assertNotNull(fileType);
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	@Test
	public void searchOffsetGTZeroFromByteArray() throws Exception {
		String[] fileNames = new String[] {
				"custom/input/file-type/Resume.doc",// 0
				"custom/input/file-type/Resume.xls",// 1
				"custom/input/file-type/Resume.ppt",// 2
				"custom/input/file-type/Resume.vsd",// 3
		};
		// 指定檔案
		String fileName = fileNames[0];
		// 讀檔
		byte[] value = FileUtils.getBytes(new File(fileName));
		FileType fileType = FileTypeUtil.searchOffsetGTZero(value);
		System.out.println(fileName + ", " + fileType);
		assertNotNull(fileType);
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	@Test
	public void searchOffsetLTZero() throws Exception {
		String[] fileNames = new String[] {
				//
				"custom/input/file-type/Tulips.bmp",// 0
				"custom/input/file-type/Tulips.gif",// 1
				"custom/input/file-type/Tulips.tga",// 2 檔尾
		};
		// 指定檔案
		String fileName = fileNames[2];
		FileType fileType = FileTypeUtil
				.searchOffsetLTZero(new BufferedInputStream(
						new FileInputStream(fileName)));
		System.out.println(fileName + ", " + fileType);
		assertNotNull(fileType);
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	@Test
	public void searchOffsetLTZeroFromByteArray() throws Exception {
		String[] fileNames = new String[] {
				//
				"custom/input/file-type/Tulips.bmp",// 0
				"custom/input/file-type/Tulips.gif",// 1
				"custom/input/file-type/Tulips.tga",// 2 檔尾
		};
		// 指定檔案
		String fileName = fileNames[2];
		// 讀檔
		byte[] value = FileUtils.getBytes(new File(fileName));
		FileType fileType = FileTypeUtil.searchOffsetLTZero(value);
		System.out.println(fileName + ", " + fileType);
		assertNotNull(fileType);
	}

	// // ------------------------------------
	// // isXxx => true/false
	// // ---------------------------------------------------
	// public static class IsFileTypeTest extends FileTypeUtilTest {
	//
	// // ---------------------------------------------------
	// // Image
	// // ---------------------------------------------------
	// @BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency =
	// 100)
	// @Test
	// public void isBmp() throws Exception {
	// String[] fileNames = new String[] {
	// //
	// "custom/input/file-type/Tulips.bmp",// 0
	// "custom/input/file-type/Tulips.tga",// 1 檔尾
	// };
	// // 指定檔案
	// String fileName = fileNames[0];
	// boolean result = FileTypeUtil.isBmp(new BufferedInputStream(
	// new FileInputStream(fileName)));
	// // System.out.println(result);
	// assertTrue(result);
	// //
	// fileName = fileNames[1];
	// result = FileTypeUtil.isBmp(new BufferedInputStream(
	// new FileInputStream(fileName)));
	// assertFalse(result);
	// }
	//
	// @BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency =
	// 100)
	// @Test
	// public void isBmpFromByteArray() throws Exception {
	// String[] fileNames = new String[] {
	// //
	// "custom/input/file-type/Tulips.bmp",// 0
	// "custom/input/file-type/Tulips.tga",// 1 檔尾
	// };
	// // 指定檔案
	// String fileName = fileNames[0];
	// // 讀檔
	// byte[] value = FileUtils.getBytes(new File(fileName));
	// boolean result = FileTypeUtil.isBmp(value);
	// // System.out.println(result);
	// assertTrue(result);
	// //
	// fileName = fileNames[1];
	// value = FileUtils.getBytes(new File(fileName));
	// result = FileTypeUtil.isBmp(value);
	// assertFalse(result);
	// }
	//
	// @BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency =
	// 100)
	// @Test
	// // round: 0.22 [+- 0.06], round.block: 0.01 [+- 0.00], round.gc: 0.00
	// // [+- 0.00], GC.calls: 6, GC.time: 0.06, time.total: 0.32, time.warmup:
	// // 0.00, time.bench: 0.32
	// public void isGif() throws Exception {
	// String[] fileNames = new String[] {
	// //
	// "custom/input/file-type/Tulips.gif",// 0
	// "custom/input/file-type/Tulips.tga",// 1 檔尾
	// };
	// // 指定檔案
	// String fileName = fileNames[0];
	// boolean result = FileTypeUtil.isGif(new BufferedInputStream(
	// new FileInputStream(fileName)));
	// // System.out.println(result);
	// assertTrue(result);
	// //
	// fileName = fileNames[1];
	// result = FileTypeUtil.isGif(new BufferedInputStream(
	// new FileInputStream(fileName)));
	// assertFalse(result);
	// }
	//
	// @BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency =
	// 100)
	// @Test
	// public void isGifFromByteArray() throws Exception {
	// String[] fileNames = new String[] {
	// //
	// "custom/input/file-type/Tulips.gif",// 0
	// "custom/input/file-type/Tulips.tga",// 1 檔尾
	// };
	// // 指定檔案
	// String fileName = fileNames[0];
	// // 讀檔
	// byte[] value = FileUtils.getBytes(new File(fileName));
	// boolean result = FileTypeUtil.isGif(value);
	// // System.out.println(result);
	// assertTrue(result);
	// //
	// fileName = fileNames[1];
	// value = FileUtils.getBytes(new File(fileName));
	// result = FileTypeUtil.isGif(value);
	// assertFalse(result);
	// }
	//
	// @BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency =
	// 100)
	// @Test
	// public void isJp2() throws Exception {
	// String[] fileNames = new String[] {
	// //
	// "custom/input/file-type/Tulips.jp2",// 0
	// "custom/input/file-type/Tulips.tga",// 1 檔尾
	// };
	// // 指定檔案
	// String fileName = fileNames[0];
	// boolean result = FileTypeUtil.isJp2(new BufferedInputStream(
	// new FileInputStream(fileName)));
	// // System.out.println(result);
	// assertTrue(result);
	// //
	// fileName = fileNames[1];
	// result = FileTypeUtil.isJp2(new BufferedInputStream(
	// new FileInputStream(fileName)));
	// assertFalse(result);
	// }
	//
	// @BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency =
	// 100)
	// @Test
	// public void isJp2FromByteArray() throws Exception {
	// String[] fileNames = new String[] {
	// //
	// "custom/input/file-type/Tulips.jp2",// 0
	// "custom/input/file-type/Tulips.tga",// 1 檔尾
	// };
	// // 指定檔案
	// String fileName = fileNames[0];
	// // 讀檔
	// byte[] value = FileUtils.getBytes(new File(fileName));
	// boolean result = FileTypeUtil.isJp2(value);
	// // System.out.println(result);
	// assertTrue(result);
	// //
	// fileName = fileNames[1];
	// value = FileUtils.getBytes(new File(fileName));
	// result = FileTypeUtil.isJp2(value);
	// assertFalse(result);
	// }
	//
	// @BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency =
	// 100)
	// @Test
	// // round: 0.28 [+- 0.03], round.block: 0.01 [+- 0.00], round.gc: 0.00
	// // [+- 0.00], GC.calls: 6, GC.time: 0.07, time.total: 0.32, time.warmup:
	// // 0.00, time.bench: 0.32
	// public void isJpg() throws Exception {
	// String[] fileNames = new String[] {
	// //
	// "custom/input/file-type/Tulips.jpg",// 0
	// "custom/input/file-type/Tulips.tga",// 1 檔尾
	// };
	// // 指定檔案
	// String fileName = fileNames[0];
	// boolean result = FileTypeUtil.isJpg(new BufferedInputStream(
	// new FileInputStream(fileName)));
	// // System.out.println(result);
	// assertTrue(result);
	// //
	// fileName = fileNames[1];
	// result = FileTypeUtil.isJpg(new BufferedInputStream(
	// new FileInputStream(fileName)));
	// assertFalse(result);
	// }
	//
	// @BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency =
	// 100)
	// @Test
	// public void isJpgFromByteArray() throws Exception {
	// String[] fileNames = new String[] {
	// //
	// "custom/input/file-type/Tulips.jpg",// 0
	// "custom/input/file-type/Tulips.tga",// 1 檔尾
	// };
	// // 指定檔案
	// String fileName = fileNames[0];
	// // 讀檔
	// byte[] value = FileUtils.getBytes(new File(fileName));
	// boolean result = FileTypeUtil.isJpg(value);
	// // System.out.println(result);
	// assertTrue(result);
	// //
	// fileName = fileNames[1];
	// value = FileUtils.getBytes(new File(fileName));
	// result = FileTypeUtil.isJpg(value);
	// assertFalse(result);
	// }
	//
	// @BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency =
	// 100)
	// @Test
	// // round: 0.26 [+- 0.04], round.block: 0.01 [+- 0.00], round.gc: 0.00
	// // [+- 0.00], GC.calls: 6, GC.time: 0.06, time.total: 0.31, time.warmup:
	// // 0.00, time.bench: 0.31
	// public void isPcx() throws Exception {
	// String[] fileNames = new String[] {
	// //
	// "custom/input/file-type/Tulips.pcx",// 0
	// "custom/input/file-type/Tulips.tga",// 1 檔尾
	// };
	// // 指定檔案
	// String fileName = fileNames[0];
	// boolean result = FileTypeUtil.isPcx(new BufferedInputStream(
	// new FileInputStream(fileName)));
	// // System.out.println(result);
	// assertTrue(result);
	// //
	// fileName = fileNames[1];
	// result = FileTypeUtil.isPcx(new BufferedInputStream(
	// new FileInputStream(fileName)));
	// assertFalse(result);
	// }
	//
	// @BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency =
	// 100)
	// @Test
	// public void isPcxFromByteArray() throws Exception {
	// String[] fileNames = new String[] {
	// //
	// "custom/input/file-type/Tulips.pcx",// 0
	// "custom/input/file-type/Tulips.tga",// 1 檔尾
	// };
	// // 指定檔案
	// String fileName = fileNames[0];
	// // 讀檔
	// byte[] value = FileUtils.getBytes(new File(fileName));
	// boolean result = FileTypeUtil.isPcx(value);
	// // System.out.println(result);
	// assertTrue(result);
	// //
	// fileName = fileNames[1];
	// value = FileUtils.getBytes(new File(fileName));
	// result = FileTypeUtil.isPcx(value);
	// assertFalse(result);
	// }
	//
	// @BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency =
	// 100)
	// @Test
	// public void isPng() throws Exception {
	// String[] fileNames = new String[] {
	// //
	// "custom/input/file-type/Tulips.png",// 0
	// "custom/input/file-type/Tulips.tga",// 1 檔尾
	// };
	// // 指定檔案
	// String fileName = fileNames[0];
	// boolean result = FileTypeUtil.isPng(new BufferedInputStream(
	// new FileInputStream(fileName)));
	// // System.out.println(result);
	// assertTrue(result);
	// //
	// fileName = fileNames[1];
	// result = FileTypeUtil.isPng(new BufferedInputStream(
	// new FileInputStream(fileName)));
	// assertFalse(result);
	// }
	//
	// @BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency =
	// 100)
	// @Test
	// public void isPngFromByteArray() throws Exception {
	// String[] fileNames = new String[] {
	// //
	// "custom/input/file-type/Tulips.png",// 0
	// "custom/input/file-type/Tulips.tga",// 1 檔尾
	// };
	// // 指定檔案
	// String fileName = fileNames[0];
	// // 讀檔
	// byte[] value = FileUtils.getBytes(new File(fileName));
	// boolean result = FileTypeUtil.isPng(value);
	// // System.out.println(result);
	// assertTrue(result);
	// //
	// fileName = fileNames[1];
	// value = FileUtils.getBytes(new File(fileName));
	// result = FileTypeUtil.isPng(value);
	// assertFalse(result);
	// }
	//
	// @BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency =
	// 100)
	// @Test
	// public void isTif() throws Exception {
	// String[] fileNames = new String[] {
	// //
	// "custom/input/file-type/Tulips.tif",// 0
	// "custom/input/file-type/Tulips.tga",// 1 檔尾
	// };
	// // 指定檔案
	// String fileName = fileNames[0];
	// boolean result = FileTypeUtil.isTif(new BufferedInputStream(
	// new FileInputStream(fileName)));
	// // System.out.println(result);
	// assertTrue(result);
	// //
	// fileName = fileNames[1];
	// result = FileTypeUtil.isTif(new BufferedInputStream(
	// new FileInputStream(fileName)));
	// assertFalse(result);
	// }
	//
	// @BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency =
	// 100)
	// @Test
	// public void isTifFromByteArray() throws Exception {
	// String[] fileNames = new String[] {
	// //
	// "custom/input/file-type/Tulips.tif",// 0
	// "custom/input/file-type/Tulips.tga",// 1 檔尾
	// };
	// // 指定檔案
	// String fileName = fileNames[0];
	// // 讀檔
	// byte[] value = FileUtils.getBytes(new File(fileName));
	// boolean result = FileTypeUtil.isTif(value);
	// // System.out.println(result);
	// assertTrue(result);
	// //
	// fileName = fileNames[1];
	// value = FileUtils.getBytes(new File(fileName));
	// result = FileTypeUtil.isTif(value);
	// assertFalse(result);
	// }
	//
	// // ---------------------------------------------------
	// // Office
	// // ---------------------------------------------------
	// @BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency =
	// 100)
	// @Test
	// public void isWord2003() throws Exception {
	// String[] fileNames = new String[] {
	// //
	// "custom/input/file-type/Resume.doc",// 0
	// "custom/input/file-type/Tulips.tga",// 1 檔尾
	// };
	// // 指定檔案
	// String fileName = fileNames[0];
	// boolean result = FileTypeUtil.isWord2003(new BufferedInputStream(
	// new FileInputStream(fileName)));
	// // System.out.println(result);
	// assertTrue(result);
	// //
	// fileName = fileNames[1];
	// result = FileTypeUtil.isWord2003(new BufferedInputStream(
	// new FileInputStream(fileName)));
	// assertFalse(result);
	// }
	//
	// @BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency =
	// 100)
	// @Test
	// public void isExcel2003() throws Exception {
	// String[] fileNames = new String[] {
	// //
	// "custom/input/file-type/Resume.xls",// 0
	// "custom/input/file-type/Tulips.tga",// 1 檔尾
	// };
	// // 指定檔案
	// String fileName = fileNames[0];
	// boolean result = FileTypeUtil.isExcel2003(new BufferedInputStream(
	// new FileInputStream(fileName)));
	// // System.out.println(result);
	// assertTrue(result);
	// //
	// fileName = fileNames[1];
	// result = FileTypeUtil.isExcel2003(new BufferedInputStream(
	// new FileInputStream(fileName)));
	// assertFalse(result);
	// }
	//
	// @BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency =
	// 100)
	// @Test
	// public void isPowerPoint2003() throws Exception {
	// String[] fileNames = new String[] {
	// //
	// "custom/input/file-type/Resume.ppt",// 0
	// "custom/input/file-type/Tulips.tga",// 1 檔尾
	// };
	// // 指定檔案
	// String fileName = fileNames[0];
	// boolean result = FileTypeUtil
	// .isPowerPoint2003(new BufferedInputStream(
	// new FileInputStream(fileName)));
	// // System.out.println(result);
	// assertTrue(result);
	// //
	// fileName = fileNames[1];
	// result = FileTypeUtil.isPowerPoint2003(new BufferedInputStream(
	// new FileInputStream(fileName)));
	// assertFalse(result);
	// }
	// }
}
