package ddd.infrastructure;

import ddd.domain.IssueNumber;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class IssueNumberConverter implements AttributeConverter<IssueNumber, Integer> {


    @Override
    public Integer convertToDatabaseColumn(IssueNumber issueNumber) {
        return issueNumber.value();
    }

    @Override
    public IssueNumber convertToEntityAttribute(Integer value) {
        return new IssueNumber(value);
    }
}