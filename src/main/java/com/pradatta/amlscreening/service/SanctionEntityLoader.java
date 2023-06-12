package com.pradatta.amlscreening.service;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.pradatta.amlscreening.jpa.datamodel.FinancialSanctionedEntity;
import com.pradatta.amlscreening.jpa.datamodel.SanctionEntityType;
import com.pradatta.amlscreening.jpa.repository.FinancialSanctionedEntityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Component
public class SanctionEntityLoader {

    private static final Logger log = LoggerFactory.getLogger(SanctionEntityLoader.class);

    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    @Value("${aml-screening.sanctions.list.url:https://webgate.ec.europa.eu/fsd/fsf/public/files/csvFullSanctionsList/content?token=dG9rZW4tMjAxNw}")
    private String sanctionListUrl;

    @Autowired
    private FinancialSanctionedEntityRepository financialSanctionedEntityRepository;


    /**
     *
     * Right now this loads the whole file into the data without much sanitation checks
     * Potentially we need to check for duplicate file
     * Avoid inserting duplicate entry to the database
     * Need to find if there's any unique id per entity and only insert/update
     *
     * @throws MalformedURLException
     */
    public void loadSanctionFile() throws MalformedURLException {
        try (InputStream input = new URL(sanctionListUrl).openStream()) {
            Reader reader = new InputStreamReader(input, "UTF-8");
            CSVParser parser = new CSVParserBuilder()
                    .withSeparator(';')
                    .withIgnoreQuotations(true)
                    .build();

            try (CSVReader csvReader = new CSVReaderBuilder(reader)
                    .withSkipLines(1)
                    .withCSVParser(parser)
                    .build()) {
                String[] line;

                while ((line = csvReader.readNext()) != null) {
                    FinancialSanctionedEntity sanctionedEntityFromCSVRow = getSanctionedEntityFromCSVRow(line);
                    sanctionedEntityFromCSVRow = financialSanctionedEntityRepository.save(sanctionedEntityFromCSVRow);
                    log.info("Saved sanctioned entity: ID ==> " + sanctionedEntityFromCSVRow.getId());
                }
            }
            financialSanctionedEntityRepository.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private FinancialSanctionedEntity getSanctionedEntityFromCSVRow(String[] row) {
        SanctionEntityType sanctionEntityType = SanctionEntityType.getEnum(row[2]);
        Date publicationDate;
        try {
            publicationDate = formatter.parse(row[4]);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }


        return new FinancialSanctionedEntity(
                sanctionEntityType,
                Long.valueOf(row[1]),
                row[15],
                row[16],
                row[14],
                row[17],
                publicationDate,
                row[5]);
    }
}
