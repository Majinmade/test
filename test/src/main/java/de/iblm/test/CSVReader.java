package de.iblm.test;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class CSVReader {

	private Date lastDate;
	private Map<String, String> mappedCols;
	public Map<String, String> getMappedCols() {
		return mappedCols;
	}
	private File tmpPath = new File(System.getProperty("java.io.tmpdir"));
	
	public void readCsv(CharSequence stringURL) {
		Date now = new Date(System.currentTimeMillis());
		if(lastDate != null && lastDate.before(now)) {
			return;
		}
		downloadFileInto(stringURL, tmpPath);
		mappedCols = new HashMap<String, String>();
		Map<Integer, String> indexCols = new HashMap<Integer, String>();

		BufferedReader br = null;
		try {
		    String sCurrentLine;
		    br = new BufferedReader(new FileReader(tmpPath.getAbsolutePath() + "/eurofxref.csv"));
		    boolean firstLine = true;
		    while ((sCurrentLine = br.readLine()) != null) {
		        String[] fields = sCurrentLine.split(",");
		        for (int i = 0; i < fields.length; i++) {
		            if (firstLine) {
		            	String currency = fields[i].trim();
		            	if(currency.length() > 0) {
			                indexCols.put(i, fields[i].trim());
			                mappedCols.put(fields[i].trim(), "");
		            	}
		            	
		            } else {
		                String colName = indexCols.get(i);
		                if (colName == null) {
		                    break;
		                }
		                if(colName.equals("Date")) {
		            		lastDate = new Date(fields[i].trim());
		            	}
		                mappedCols.put(colName, fields[i].trim());
		            }
		        }
		        firstLine = false;
		    }
		} catch (IOException e) {
		    e.printStackTrace();
		} finally {
		    try {
		        if (br != null)
		            br.close();
		    } catch (IOException ex) {
		        ex.printStackTrace();
		    }
		}
	}
	
	private void downloadFileInto(CharSequence stringURL,
            File directory) {
        try {
            URL url = new URL(stringURL.toString());
            unzipIntoDirectory(url.openStream(), directory);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	private void unzipIntoDirectory(File file, File directory) {
        try {
            unzipIntoDirectory(new FileInputStream(file), directory);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
	private void unzipIntoDirectory(InputStream inputStream,
            File directory) {
        if (directory.isFile())
            return;
        directory.mkdirs();

        try {
            inputStream = new BufferedInputStream(inputStream);
            inputStream = new ZipInputStream(inputStream);

            for (ZipEntry entry = null; (entry = ((ZipInputStream) inputStream)
                    .getNextEntry()) != null;) {
                StringBuilder pathBuilder = new StringBuilder(
                        directory.getPath()).append('/').append(
                        entry.getName());
                File file = new File(pathBuilder.toString());

                if (entry.isDirectory()) {
                    file.mkdirs();
                    continue;
                }

                Files.copy(inputStream, Paths.get(pathBuilder.toString()), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
    }
}
