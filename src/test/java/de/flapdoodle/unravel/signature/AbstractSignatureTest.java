package de.flapdoodle.unravel.signature;

import io.vavr.collection.List;

public abstract class AbstractSignatureTest {
	protected  static VisibleClass visibleClass(ClassName className , VisibleMethod ... methods) {
		return VisibleClass.builder()
				.isArray(false)
				.visibility(Visibility.Public)
				.name(className)
				.methods(List.of(methods))
				.build();
	}
	
	protected  static VisibleMethod visibleMethod(SimpleType returnType, String name, SimpleType ... parameters) {
		return visibleMethod(false, returnType, name, parameters);
	}
	
	protected  static VisibleMethod visibleMethod(boolean isStatic, SimpleType returnType, String name, SimpleType ... parameters) {
		return VisibleMethod.builder()
				.isStatic(isStatic)
				.visibility(Visibility.Public)
				.returnType(returnType)
				.name(name)
				.parameterTypes(List.of(parameters))
				.build();
	}
	
	protected  static ClassName className(String packageName, String name) {
		return ClassName.of(packageName, name);
	}
	
	protected static SimpleType typeOf(Class<?> type) {
		Package p = type.getPackage();
		String pname="";
		if (p!=null) {
			pname=p.getName();
		}
		
		if (type.isArray()) {
			return SimpleType.arrayOf(pname, type.getSimpleName());
		}
		return SimpleType.of(pname, type.getSimpleName());
	}

}
