package se.jbee.jar;

import static se.jbee.jar.Archive.archive;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import se.jbee.jar.bytecode.ClassInputStream;
import se.jbee.jar.bytecode.Classfile;

public class JarScanner {

	private static final Pattern ARCHIVES = Pattern.compile( "\\.(?:zip|jar|war|ear)$" );

	private final JarProcessor out;

	public JarScanner( JarProcessor out ) {
		super();
		this.out = out;
	}

	public void scan( String file )
			throws IOException {
		if ( isArchiveFile( file ) ) {
			scan( new ZipFile( file ) );
		}
	}

	public void scan( ZipFile zip )
			throws IOException {
		Enumeration<? extends ZipEntry> entries = zip.entries();
		while ( entries.hasMoreElements() ) {
			ZipEntry entry = entries.nextElement();
			if ( !entry.isDirectory() ) {
				String name = entry.getName();
				if ( isClassFile( name ) ) {
					scan( zip.getInputStream( entry ) );
				} else if ( isArchiveFile( name ) && out.filter().process( archive( name ) ) ) {
					ZipInputStream in = new ZipInputStream( zip.getInputStream( entry ) );
					entry = in.getNextEntry();
					while ( entry != null ) {
						if ( isClassFile( entry.getName() ) ) {
							scan( in );
						}
						in.closeEntry();
						entry = in.getNextEntry();
					}
					in.close();
				}
			}
		}
	}

	private void scan( InputStream classInputStream )
			throws IOException {
		Classfile.readClassfile( new ClassInputStream( classInputStream ), out );
	}

	static boolean isClassFile( String name ) {
		return name.endsWith( ".class" );
	}

	static boolean isArchiveFile( String name ) {
		return ARCHIVES.matcher( name ).find();
	}

}