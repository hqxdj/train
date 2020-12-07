package com.macro.mall.tiny.aop;

import java.lang.annotation.*;

@Target({ ElementType.TYPE, ElementType.METHOD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AnnotationAop {
}
