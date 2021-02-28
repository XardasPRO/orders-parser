package com.unlimint.test.model.reader;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class LineRead {
	String line;
	String filename;
	Long lineNumber;
	FileType fileType;
}
