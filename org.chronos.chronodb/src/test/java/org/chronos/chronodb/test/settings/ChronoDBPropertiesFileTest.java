package org.chronos.chronodb.test.settings;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.chronos.chronodb.api.ChronoDB;
import org.chronos.chronodb.internal.api.ChronoDBInternal;
import org.chronos.chronodb.internal.impl.engines.inmemory.InMemoryChronoDB;
import org.chronos.chronodb.test.base.ChronoDBUnitTest;
import org.chronos.common.test.junit.categories.IntegrationTest;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(IntegrationTest.class)
public class ChronoDBPropertiesFileTest extends ChronoDBUnitTest {

	@Test
	public void loadingPartialChronoConfigFromPropertiesFileWorks() {
		File testDirectory = tempDir;
		File dbFile = new File(testDirectory, UUID.randomUUID().toString().replaceAll("-", "") + ".chronodb");
		try {
			dbFile.createNewFile();
		} catch (IOException e) {
			fail(e.toString());
		}
		File propertiesFile = this.getSrcTestResourcesFile("chronotest.properties");
		ChronoDB chronoDB = ChronoDB.FACTORY.create().embeddedDatabase(dbFile).withPropertiesFile(propertiesFile)
				.build();
		assertNotNull(chronoDB);
		try {
			// assert that the property in the file was applied
			ChronoDBInternal chronoDBinternal = (ChronoDBInternal) chronoDB;
			assertFalse(chronoDBinternal.getConfiguration().isBlindOverwriteProtectionEnabled());
		} finally {
			chronoDB.close();
		}
	}

	@Test
	public void loadingFullChronoConfigFromPropertiesFileWorks() {
		File testDirectory = tempDir;
		File dbFile = new File(testDirectory, UUID.randomUUID().toString().replaceAll("-", "") + ".chronodb");
		try {
			dbFile.createNewFile();
		} catch (IOException e) {
			fail(e.toString());
		}
		File propertiesFile = this.getSrcTestResourcesFile("fullChronoConfig.properties");
		ChronoDB chronoDB = ChronoDB.FACTORY.create().fromPropertiesFile(propertiesFile).build();
		assertNotNull(chronoDB);
		try {
			// assert that the correct type of database was instantiated
			assertTrue(chronoDB instanceof InMemoryChronoDB);
			// assert that the property in the file was applied
			ChronoDBInternal chronoDBinternal = (ChronoDBInternal) chronoDB;
			assertFalse(chronoDBinternal.getConfiguration().isBlindOverwriteProtectionEnabled());
		} finally {
			chronoDB.close();
		}
	}
}