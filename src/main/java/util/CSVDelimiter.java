package util;

public enum CSVDelimiter {
		TAB('\t'),
		COMMA(','),
		SEMICOLON(';');
	
		private char delimiter;
		
		CSVDelimiter(char delimiter){
			this.delimiter = delimiter;
		}

		public String getDelimiter() {
			return String.format("%s",this.delimiter);
		}

		
}
