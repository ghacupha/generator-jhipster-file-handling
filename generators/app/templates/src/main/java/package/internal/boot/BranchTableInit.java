package io.github.deposits.app.boot;

import io.github.deposits.repository.BranchTableRepository;
import io.github.deposits.service.BranchTableService;
import io.github.deposits.service.dto.BranchTableDTO;
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
public class BranchTableInit implements ApplicationRunner {

    private final BranchTableService branchTableService;
    private final BranchTableRepository branchTableRepository;

    public BranchTableInit(final BranchTableService branchTableService, final BranchTableRepository branchTableRepository) {
        this.branchTableService = branchTableService;
        this.branchTableRepository = branchTableRepository;
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
        if (branchTableRepository.count() == 0) {
            addedItems = getBranchTableItems().stream().peek(branchTableService::save).count();
        }

        log.info("{} items initialized in the type-table", addedItems);
    }

    private List<BranchTableDTO> getBranchTableItems() {
        List<BranchTableDTO> branchList = new ArrayList<>();

        branchList.add(BranchTableDTO.builder().branchName("HEAD OFFICE").serviceOutletCode("998").county("NAIROBI").town("NAIROBI").yearOpened(1985).build());
        branchList.add(BranchTableDTO.builder().branchName("KOINANGE STREET BRANCH").serviceOutletCode("000").county("NAIROBI").town("NAIROBI").yearOpened(1985).build());
        branchList.add(BranchTableDTO.builder().branchName("WESTLANDS BRANCH").serviceOutletCode("001").county("NAIROBI").town("NAIROBI").yearOpened(1992).build());
        branchList.add(BranchTableDTO.builder().branchName("INDUSTRIAL AREA BRANCH").serviceOutletCode("002").county("NAIROBI").town("NAIROBI").yearOpened(1995).build());
        branchList.add(BranchTableDTO.builder().branchName("MOMBASA BRANCH").serviceOutletCode("003").county("MOMBASA").town("MOMBASA").yearOpened(1993).build());
        branchList.add(BranchTableDTO.builder().branchName("KISUMU BRANCH").serviceOutletCode("004").county("KISUMU").town("KISUMU").yearOpened(1995).build());
        branchList.add(BranchTableDTO.builder().branchName("ELDORET BRANCH").serviceOutletCode("005").county("UASIN GISHU").town("ELDORET").yearOpened(1997).build());
        branchList.add(BranchTableDTO.builder().branchName("MERU BRANCH").serviceOutletCode("006").county("MERU").town("MERU").yearOpened(1997).build());
        branchList.add(BranchTableDTO.builder().branchName("LIBRA BRANCH").serviceOutletCode("007").county("NAIROBI").town("NAIROBI").yearOpened(2008).build());
        branchList.add(BranchTableDTO.builder().branchName("NAKURU BRANCH").serviceOutletCode("008").county("NAIROBI").town("NAKURU").yearOpened(2008).build());
        branchList.add(BranchTableDTO.builder().branchName("LAMU BRANCH").serviceOutletCode("009").county("LAMU").town("LAMU").yearOpened(2011).build());
        branchList.add(BranchTableDTO.builder().branchName("THIKA ROAD MALL BRANCH").serviceOutletCode("010").county("NAIROBI").town("NAIROBI").yearOpened(2016).build());
        branchList.add(BranchTableDTO.builder().branchName("GREEN HOUSE MALL BRANCH").serviceOutletCode("011").county("NAIROBI").town("NAIROBI").yearOpened(2016).build());

        return branchList;
    }
}
