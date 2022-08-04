package com.softline.csrv.enums;

/**
 *  Enum, в котором code Виды документов
 */
public enum DocKindCode {

    ALBUM_TFF("ALBUM_TFF"),                                  // Альбом ТФФ
    SHEET_TD("SHEET_TD"),                                    // Ведомость технического проекта
    SHEET_OD("SHEET_OD"),                                    // Ведомость эксплуатационных документов
    REVISION("REVISION"),                                    // Доработка
    ZNA("ZNA"),                                              // ЗНА
    USER_MANUAL("USER_MANUAL"),                              // Инструкция по эксплуатации
    CORRECTION("CORRECTION"),                                // Исправление
    LOT_SAM("LOT_SAM"),                                      // ЛОТ SAM
    MFT("MFT"),                                              // Методика ФТ
    MCLI("MCLI"),                                            // Методика расчета трудоемкости
    OPZ("OPZ"),                                              // ОПЗ
    GDS("GDS"),                                              // Общее описание системы
    DESC_AF("DESC_AF"),                                      // Описание автоматизируемых функций
    DESC_IS("DESC_IS"),                                      // Описание информационного обеспечения
    DESC_TC("DESC_TC"),                                      // Описание комплекса технических средств
    DESC_O("DESC_O"),                                        // Описание организации информационной базы
    DESC_S("DESC_S"),                                        // Описание программного обеспечения
    PASSPORT("PASSPORT"),                                    // Паспорт
    IT_SERVICE_PASSPORT("IT_SERVICE_PASSPORT"),              // Паспорт ИТ-сервиса
    PIM("PIM"),                                              // ПиМ
    OMP("OMP"),                                              // Порядок эксплуатации и технического обслуживания
    ENTP("ENTP"),                                            // Пояснительная записка к техническому проекту
    DASR("DASR"),                                            // Проектная оценка надежности системы
    GUID_SA("GUID_SA"),                                      // Руководство по администрированию системы
    GUIDE_SC("GUIDE_SC"),                                    // Руководство по пуско-наладке системы
    GUIDE_USER("GUIDE_USER"),                                // Руководство пользователя
    GUIDE_SP("GUIDE_SP"),                                    // Руководство системного программиста
    COORDINATION("COORDINATION"),                            // Согласование
    VIS_APPROVAL("VIS_APPROVAL"),                            // Согласование ВИС
    SPECIFICATION("SPECIFICATION"),                          // Спецификация
    KBA("KBA"),                                              // Статья базы знаний
    SDCTM("SDCTM"),                                          // Схема структурная комплекса технических средств
    TZ_CHTZ("TZ_CHTZ"),                                      // ТЗ/ЧТЗ
    TABLE_CONNECTIONS("TABLE_CONNECTIONS"),                  // Таблица соединений и подключений
    TECHNICAL_REQUIREMENT("TECHNICAL_REQUIREMENT"),          // Техническое требование
    TECHNOLOGICAL_REGULATIONS("TECHNOLOGICAL_REGULATIONS"),  // Технологический регламент
    REQUIREMENT("REQUIREMENT");                              // Требование


    private final String code;

    DocKindCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}