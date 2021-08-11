package micdoodle8.mods.miccore.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import net.minecraftforge.fml.relauncher.Side;

@Retention(RUNTIME)
@Target(METHOD)
public @interface Network {
	Side targetSide();
}
