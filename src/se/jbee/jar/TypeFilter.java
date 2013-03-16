package se.jbee.jar;

public interface TypeFilter {

	static final TypeFilter ALL = new TypeFilter() {

		@Override
		public boolean process( Type type ) {
			return true;
		}

		@Override
		public boolean process( Archive archive ) {
			return true;
		}
	};

	boolean process( Archive archive );

	boolean process( Type type );
}
