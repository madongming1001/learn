package com.madm.design.visitor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Principal implements Visitor {
    public void visit(Student student) {
        log.info("学生信息，姓名：{} 班级：{}", student.name,
                student.clazz);
    }

    public void visit(Teacher teacher) {
        log.info("学生信息，姓名：{} 班级：{} 升学率: {}", teacher.name,
                teacher.clazz, teacher.entranceRatio());
    }
}
