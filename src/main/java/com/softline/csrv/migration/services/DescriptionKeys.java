package com.softline.csrv.migration.services;

import java.util.HashMap;
import java.util.Map;

public enum DescriptionKeys {

    KEY1("Влияние вносимых изменений на другие подсистемы и необходимость привлечения соисполнителя"),
    KEY2("Изменения политик мониторинга"),
    KEY3("Краткое описание реализации"),
    KEY4("Необходима остановка сервиса"),
    KEY5("Ожидаемый результат"),
    KEY6("Описание проблемы"),
    KEY7("Описание требования"),
    KEY8("Описание работ по обновлению"),
    KEY9("Период проведения технологической паузы (начало)"),
    KEY10("ПиМ апробации"),
    KEY11("Планируемый результат"),
    KEY12("Причина возникновения"),
    KEY13("Причины проведения работ и краткое описание"),
    KEY14("Сборка к установке"),
    KEY15("Способ повторения"),
    KEY16("Описание RN (RO)"),
    KEY17("Степень воздействия"),
    KEY18("Технология восстановления системы в исходное состояние"),
    KEY19("Фактический результат"),
    KEY20("Виды тестирования"),
    KEY21("Затрагиваемые сервисы"),
    KEY22("ИТ-сервис"),
    KEY23("Классификация"),
    KEY24("Контактное лицо от РП для ОТС/ОПС"),
    KEY25("Контур"),
    KEY26("Место проведения работ"),
    KEY27("Недоступность пользовательских сервисов"),
    KEY28("Оборудование"),
    KEY29("Среда"),
    KEY30("Среды"),
    KEY31("Тип работ"),
    KEY32("Период проведения технологической паузы (окончание)"),
    KEY33("Способ выполнения работ");

    String key;

    private static class Holder{
        final static Map<String, DescriptionKeys> KEY_MAP = new HashMap<>();
    }

    DescriptionKeys(String key){
        this.key = key;
        DescriptionKeys.Holder.KEY_MAP.put(key, this);
    }

    public static DescriptionKeys parseKey(String key){
        return DescriptionKeys.Holder.KEY_MAP.get(key);
    }

    public String getKey() {
        return key;
    }
}
