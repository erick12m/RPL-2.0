package com.example.rpl.RPL.config.persistance.converter;


import com.example.rpl.RPL.model.Language;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class LanguageConverter implements AttributeConverter<Language, String> {

    @Override
    public String convertToDatabaseColumn(Language language) {
        if (language == null) {
            return null;
        }

        return language.getNameAndVersion();
    }

    @Override
    public Language convertToEntityAttribute(String lang_id) {
        if (lang_id != null) {
            return Language.getByNameAndVersion(lang_id);
        } else {
            return null;
        }
    }
}