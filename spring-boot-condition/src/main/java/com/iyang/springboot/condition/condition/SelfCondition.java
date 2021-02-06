package com.iyang.springboot.condition.condition;

import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class SelfCondition extends SpringBootCondition {

    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        MergedAnnotations annotations = metadata.getAnnotations();
        // StandardMethodMetadata
        System.out.println(metadata.getClass().toString());
        // TypeMappedAnnotations
        System.out.println(annotations.getClass().toString());
        annotations.stream().forEach(annotationMergedAnnotation -> {
            System.out.println(annotationMergedAnnotation);
        });

        ConditionOutcome conditionOutcome = new ConditionOutcome(false, "This is GavinYang...Info");
        return conditionOutcome;
    }
}
