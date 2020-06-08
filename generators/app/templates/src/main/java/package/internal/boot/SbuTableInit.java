package io.github.deposits.app.boot;

import io.github.deposits.repository.SbuTableRepository;
import io.github.deposits.service.SbuTableService;
import io.github.deposits.service.dto.SbuTableDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Transactional
@Service
public class SbuTableInit implements ApplicationRunner {

    private final SbuTableService sbuTableService;
    private final SbuTableRepository sbuTableRepository;

    public SbuTableInit(final SbuTableService sbuTableService, final SbuTableRepository sbuTableRepository) {
        this.sbuTableService = sbuTableService;
        this.sbuTableRepository = sbuTableRepository;
    }

    /**
     * Callback used to run the bean.
     *
     * @param args incoming application arguments
     * @throws Exception on error
     */
    @Override
    public void run(final ApplicationArguments args) throws Exception {

        long addedItems = 0;
        if (sbuTableRepository.count() == 0) {
            addedItems = getSbuTableItems().stream().peek(sbuTableService::save).count();
        }

        log.info("{} items initialized in the sbu-table", addedItems);

    }

    private List<SbuTableDTO> getSbuTableItems() {
        List<SbuTableDTO> sbus = new ArrayList<>();

        sbus.add(SbuTableDTO.builder().sbuCode("BUS").sbuName("SME").build());
        sbus.add(SbuTableDTO.builder().sbuCode("PER").sbuName("RETAIL").build());
        sbus.add(SbuTableDTO.builder().sbuCode("COR").sbuName("CORPORATE").build());
        sbus.add(SbuTableDTO.builder().sbuCode("TRE").sbuName("CORPORATE").build());
        sbus.add(SbuTableDTO.builder().sbuCode("RCV").sbuName("RETAIL").build());
        sbus.add(SbuTableDTO.builder().sbuCode("001").sbuName("RETAIL").build());
        sbus.add(SbuTableDTO.builder().sbuCode("INB").sbuName("RETAIL").build());
        sbus.add(SbuTableDTO.builder().sbuCode("null").sbuName("RETAIL").build());

        return sbus;
    }


}
