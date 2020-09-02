package com.demo.utils;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URL;
import java.util.List;

//import javax.activation.MimetypesFileTypeMap;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

@Ignore
public class FileUtilTest {

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void combine() {
		String result = null;
		//
		result = FileUtil.combine("D:/Application/EnterpriseShare.Core/MKTPLS.Service.HBase", "Inbound/Xml/Inventory");
		// D:/Application/EnterpriseShare.Core/MKTPLS.Service.HBase/Inbound/Xml/Inventory
		System.out.println(result);
		assertEquals("D:/Application/EnterpriseShare.Core/MKTPLS.Service.HBase/Inbound/Xml/Inventory", result);
		//
		result = FileUtil.combine("", "Inbound/Xml/Inventory");
		// Inbound/Xml/Inventory
		System.out.println(result);
		assertEquals("Inbound/Xml/Inventory", result);
		//
		result = FileUtil.combine(null, "Inbound/Xml/Inventory");
		System.out.println(result);
		assertEquals("Inbound/Xml/Inventory", result);
		//
		result = FileUtil.combine("D:/Application/EnterpriseShare.Core/MKTPLS.Service.HBase", null);
		System.out.println(result);
		assertEquals("D:/Application/EnterpriseShare.Core/MKTPLS.Service.HBase", result);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void getFiles() {
		List<String> result = null;
		//
		result = FileUtil.getFiles("./",
				"xml");
		System.out.println(result);
		//
		result = FileUtil.getFiles("./",
				"xml", "md");
		System.out.println(result);
	}

//	@Test
//	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
//	public void getMimeType() {
//		String filepath = "custom/input/docx/Resume.doc";
//		File f = new File(filepath);
//		String mimetype = new MimetypesFileTypeMap().getContentType(f);
//		String type = mimetype.split("/")[0];
//		// System.out.println(mimetype + ", " + type);
//	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	// round: 0.50 [+- 0.05], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 3, GC.time: 0.02, time.total: 0.52, time.warmup: 0.00,
	// time.bench: 0.52
	public void getUsableSpace() {
		Long result = null;

		result = FileUtil.getUsableSpace("D:\\");

		assertNotNull(result);
		assertTrue(result > 0L);

		System.out.println(result);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	// round: 0.51 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 0, GC.time: 0.00, time.total: 0.52, time.warmup: 0.00,
	// time.bench: 0.51
	public void renameFile() {
		File result = null;
		String originalPath = "D:\\view.tar";
		String newPath = "D:\\view1.tar";

		result = FileUtil.renameFile(originalPath, newPath);

		assertNotNull(result);
		assertEquals("view1.tar", result.getName());
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void toUrl() {
		URL result = null;

		result = FileUtil.toUrl("build.gradle");

		System.out.println(result);
		assertNotNull(result);
	}
}
