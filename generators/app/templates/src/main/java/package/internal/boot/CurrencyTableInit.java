package io.github.deposits.app.boot;

import io.github.deposits.repository.CurrencyTableRepository;
import io.github.deposits.service.CurrencyTableService;
import io.github.deposits.service.dto.CurrencyTableDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static io.github.deposits.domain.enumeration.CurrencyLocality.FOREIGN;
import static io.github.deposits.domain.enumeration.CurrencyLocality.LOCAL;

@Slf4j
@Transactional
@Service
public class CurrencyTableInit implements ApplicationRunner {

    private final CurrencyTableService currencyTableService;
    private final CurrencyTableRepository currencyTableRepository;

    public CurrencyTableInit(final CurrencyTableService currencyTableService, final CurrencyTableRepository currencyTableRepository) {
        this.currencyTableService = currencyTableService;
        this.currencyTableRepository = currencyTableRepository;
    }

    @Override
    public void run(final ApplicationArguments args) throws Exception {

        long addedItems = 0;
        if (currencyTableRepository.count() == 0) {
            addedItems = getCurrencies().stream().peek(currencyTableService::save).count();
        }

        log.info("{} items initialized in the currency-table", addedItems);
    }

    private List<CurrencyTableDTO> getCurrencies() {

        List<CurrencyTableDTO> currencies = new ArrayList<>();

        currencies.add(CurrencyTableDTO.builder().currencyCode("KES").locality(LOCAL).country("KENYA").currencyName("KENYA SHILLING").build());
        currencies.add(CurrencyTableDTO.builder().currencyCode("USD").locality(FOREIGN).country("USA").currencyName("US DOLLAR").build());
        currencies.add(CurrencyTableDTO.builder().currencyCode("GBP").locality(FOREIGN).country("UNITED KINGDOM").currencyName("STERLING POUND").build());
        currencies.add(CurrencyTableDTO.builder().currencyCode("EUR").locality(FOREIGN).country("EURO-ZONE").currencyName("EURO").build());
        currencies.add(CurrencyTableDTO.builder().currencyCode("CHF").locality(FOREIGN).country("SWITZERLAND").currencyName("SWISS FRANC").build());
        currencies.add(CurrencyTableDTO.builder().currencyCode("ZAR").locality(FOREIGN).country("SOUTH AFRICA").currencyName("SOUTH AFRICAN RAND").build());
        currencies.add(CurrencyTableDTO.builder().currencyCode("UGX").locality(FOREIGN).country("UGANDA").currencyName("UGANDAN SHILLING").build());
        currencies.add(CurrencyTableDTO.builder().currencyCode("TZS").locality(FOREIGN).country("TANZANIA").currencyName("TANZANIAN SHILLING").build());
        currencies.add(CurrencyTableDTO.builder().currencyCode("JPY").locality(FOREIGN).country("JAPAN").currencyName("JAPANESE YEN").build());
        currencies.add(CurrencyTableDTO.builder().currencyCode("INR").locality(FOREIGN).country("INDIA").currencyName("INDIAN RUPEE").build());
        currencies.add(CurrencyTableDTO.builder().currencyCode("CAD").locality(FOREIGN).country("CANADA").currencyName("CANADIAN DOLLAR").build());
        currencies.add(CurrencyTableDTO.builder().currencyCode("AED").locality(FOREIGN).country("UAE").currencyName("UAE DIRHAM").build());
        currencies.add(CurrencyTableDTO.builder().currencyCode("CNY").locality(FOREIGN).country("CHINA").currencyName("CHINESE YUAN").build());

        return currencies;
    }
}
