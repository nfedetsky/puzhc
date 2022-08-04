package com.softline.csrv.enums;

import javax.annotation.Nullable;


public enum RoleCode {

    R_ADMINISTRATOR("R_ADMINISTRATOR"),//Р.Администратор
    R_ADMINISTRATOR_KB("R_ADMINISTRATOR_KB"),//Р.Администратор БЗ
    R_ANALIST("R_ANALIST"),//Р.Аналитик
    R_TEST_ENGINEER("R_TEST_ENGINEER"),//Р.Инженер по тестированию
    R_PROJECT_CURATOR("R_PROJECT_CURATOR"),//Р.Куратор проекта
    R_PATCH_MANAGER("R_PATCH_MANAGER"),//Р.Менеджер исправлений
    R_PROJECT_ADMIN("R_PROJECT_ADMIN"),//Р.Менеджер проекта
    R_PRODUCTION_MANAGER("R_PRODUCTION_MANAGER"),//Р.Менеджер производства
    R_SOFTWARE_DEVELOPER("R_SOFTWARE_DEVELOPER"),//Р.Разработчик ПО
    R_RELEASE_MANAGER("R_RELEASE_MANAGER"),//Р.Релиз-менеджер
    R_PROJECT_MANAGER("R_PROJECT_MANAGER"),//Р.Руководитель проекта
    R_SPECIALIST("R_SPECIALIST"),//Р.Специалист
    FK_ADMINISTRATOR_KB("FK_ADMINISTRATOR_KB"),//ФК.Администратор БЗ
    FK_CE_ADMINISTRATOR("FK_CE_ADMINISTRATOR"),//ФК.Администратор ВР
    FK_ADMINISTRATOR_RRI("FK_ADMINISTRATOR_RRI"),//ФК.Администратор НСИ
    FK_IS_ARCHITECT("FK_IS_ARCHITECT"),//ФК.Архитектор ИС
    FK_IS_MAIN_TEKHNOLOGIST("FK_IS_MAIN_TEKHNOLOGIST"),//ФК.Главный технолог ИС
    FK_SUVV_DISPATCHER("FK_SUVV_DISPATCHER"),//ФК.Диспетчер СУВВ
    FK_RFC_DISPATCHER("FK_RFC_DISPATCHER"),//ФК.Диспетчер RFC
    FK_CURATOR_VIS("FK_CURATOR_VIS"),//ФК.Куратор Версий ИС
    FK_CURATOR_IS("FK_CURATOR_IS"),//ФК.Куратор ИС
    FK_IS_CURATOR_MAINTENANCE("FK_IS_CURATOR_MAINTENANCE"),//ФК.Куратор эксплуатации ИС
    FK_IS_OPERATIONS_MANAGER("FK_IS_OPERATIONS_MANAGER"),//ФК.Менеджер эксплуатации ИС
    FK_IS_FIRST_TEKHNOLOGIST("FK_IS_FIRST_TEKHNOLOGIST"),//ФК.Первый технолог ИС
    FK_IS_HEAD_DEVELOPMENT("FK_IS_HEAD_DEVELOPMENT"),//ФК.Руководитель развития ИС
    FK_SYSTEM_ADMINISTRATOR("FK_SYSTEM_ADMINISTRATOR"),//ФК.Системный администратор
    FK_SPECIALIST("FK_SPECIALIST"),//ФК.Специалист
    FK_IS_TEKHNOLOGIST("FK_IS_TEKHNOLOGIST");//ФК.Технолог ИС

    private final String code;

    RoleCode(String value) {
        this.code = value;
    }

    public String getCode() {
        return code;
    }

    @Nullable
    public static RoleCode findByCode(String code) {
        for (RoleCode at : RoleCode.values()) {
            if (at.getCode().equals(code)) {
                return at;
            }
        }
        return null;
    }
}