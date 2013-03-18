package se.jbee.cls.file;

import static se.jbee.cls.ref.ClassSignature.classSignature;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import se.jbee.cls.ref.Class;
import se.jbee.cls.ref.ClassSignature;
import se.jbee.cls.ref.Modifiers;
import se.jbee.cls.sca.JarProcessor;
import se.jbee.cls.sca.TypeFilter;

public final class Classfile {

	private static final int MAGIC_NUMBER = 0xcafebabe;

	private static final Class[] PRIMITIVES = new Class[26];

	static {
		for ( TypeCode c : TypeCode.values() ) {
			PRIMITIVES[c.name().charAt( 0 ) - 'A'] = Class.cls( c.name, 0 );
		}
	}

	/**
	 * Extracts the constant pool from the specified data stream of a class file.
	 * 
	 * @param in
	 *            Input stream of a class file starting at the first byte.
	 * @return extracted array of constants.
	 * @throws IOException
	 *             in case of reading errors or invalid class file.
	 */
	public static void readClassfile( ClassInputStream in, TypeFilter filter, JarProcessor out )
			throws IOException {
		if ( in.int32bit() != MAGIC_NUMBER ) {
			throw new IOException( "Not a class file: Expected Magic number 0xcafebabe." );
		}
		in.uint16bit(); // minor version
		in.uint16bit(); // major version
		ConstantPool cp = ConstantPool.read( in );

		Modifiers access = Modifiers.modifiers( in.uint16bit() );
		Class type = type( cp.utf0( in.uint16bit() ) );
		Class superclass = type( cp.utf0( in.uint16bit() ) );
		Class[] interfaces = readInterfaces( in, cp, in.uint16bit() );
		ClassSignature cls = classSignature( access, type, superclass, interfaces );
		if ( filter.process( cls ) ) {
			out.process( cls, cp );
			readFieldOrMethod( in, cp );
			readFieldOrMethod( in, cp );
			readAttributes( in, cp );
		}
	}

	private static void readFieldOrMethod( ClassInputStream in, ConstantPool cp )
			throws IOException {
		int count = in.uint16bit();
		for ( int i = 0; i < count; i++ ) {
			int flags = in.uint16bit();
			String name = cp.utf0( in.uint16bit() );
			String descriptor = cp.utf0( in.uint16bit() );
			readAttributes( in, cp );
		}
	}

	private static void readAttributes( ClassInputStream in, ConstantPool cp )
			throws IOException {
		int attributeCount = in.uint16bit();
		for ( int a = 0; a < attributeCount; a++ ) {
			readAttribute( cp, in );
		}
	}

	private static void readAttribute( ConstantPool cp, ClassInputStream in )
			throws IOException {
		String name = cp.utf0( in.uint16bit() );
		int length = in.int32bit();
		in.skipBytes( length );
	}

	private static Class[] readInterfaces( ClassInputStream stream, ConstantPool cp,
			int interfaceCount )
			throws IOException {
		Class[] superinterfaces = new Class[interfaceCount];
		for ( int i = 0; i < interfaceCount; i++ ) {
			superinterfaces[i] = type( cp.utf0( stream.uint16bit() ) );
		}
		return superinterfaces;
	}

	public static Class type( String name ) {
		if ( name == null || name.isEmpty() ) {
			return Class.NONE;
		}
		if ( !Character.isLowerCase( name.charAt( 0 ) ) && name.endsWith( ";" ) ) {
			return types( name )[0];
		}
		return Class.cls( name );
	}

	public static Class[] types( String descriptor ) {
		int index = 0;
		List<Class> names = new ArrayList<Class>();
		char[] dc = descriptor.toCharArray();
		int arrayDimentions = 0;
		while ( index < dc.length ) {
			final char c = dc[index++];
			if ( c == '[' ) {
				arrayDimentions++;
			} else {
				Class ref = null;
				if ( c == 'L' ) {
					int end = descriptor.indexOf( ';', index );
					String name = descriptor.substring( index, end );
					ref = Class.cls( name, arrayDimentions );
					index = end + 1;
				} else {
					ref = PRIMITIVES[c - 'A'];
				}
				names.add( ref );
				arrayDimentions = 0;
			}
		}
		return names.toArray( new Class[names.size()] );
	}

	private static enum TypeCode {
		B( byte.class ),
		C( char.class ),
		D( double.class ),
		F( float.class ),
		I( int.class ),
		J( long.class ),
		S( short.class ),
		Z( boolean.class ),
		V( void.class ),
		L( Object.class );

		final String name;

		private TypeCode( java.lang.Class<?> type ) {
			this.name = type.getCanonicalName().replace( '.', '/' ).intern();
		}

	}
}