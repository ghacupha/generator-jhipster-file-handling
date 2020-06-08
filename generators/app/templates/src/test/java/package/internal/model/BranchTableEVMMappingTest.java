package io.github.deposits.app.model;

import io.github.deposits.app.Mapping;
import io.github.deposits.service.dto.BranchTableDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BranchTableEVMMappingTest {

    private Mapping<BranchTableEVM, BranchTableDTO> mapping;

    private static final String BRANCH_1 = "BRANCH_1";
    private static final String CODE_1 = "CODE_1";
    private static final String COUNTY_1 = "COUNTY_1";
    private static final String TOWN_1 = "TOWN_1";
    private static final int YEAR_OPENED = 2019;

    // @formatter:off
    private static final BranchTableEVM BRANCH_TABLE_EVM = BranchTableEVM.builder()
                                                                         .branchName("BRANCH_1")
                                                                         .serviceOutletCode("CODE_1")
                                                                         .county("COUNTY_1")
                                                                         .town("TOWN_1")
                                                                         .yearOpened(2019)
                                                                         .build();
    // @formatter:off

    // @formatter:off
    private static final BranchTableDTO BRANCH_TABLE_DTO = BranchTableDTO.builder()
                                                                         .branchName("BRANCH_1")
                                                                         .serviceOutletCode("CODE_1")
                                                                         .county("COUNTY_1")
                                                                         .town("TOWN_1")
                                                                         .yearOpened(2019)
                                                                         .build();
    // @formatter:off

    @BeforeEach
    void setUp() {

        mapping = new BranchTableEVMMappingImpl();

    }

    @Test
    public void toValue1Conversion() {

        assertTrue(mapping.toValue1(BRANCH_TABLE_DTO).getBranchName().equalsIgnoreCase(BRANCH_1));
        assertTrue(mapping.toValue1(BRANCH_TABLE_DTO).getServiceOutletCode().equalsIgnoreCase(CODE_1));
        assertTrue(mapping.toValue1(BRANCH_TABLE_DTO).getCounty().equalsIgnoreCase(COUNTY_1));
        assertTrue(mapping.toValue1(BRANCH_TABLE_DTO).getTown().equalsIgnoreCase(TOWN_1));
        assertThat(mapping.toValue1(BRANCH_TABLE_DTO).getYearOpened()).isEqualTo(YEAR_OPENED);
    }

    @Test
    public void toValue2Conversion() {

        assertTrue(mapping.toValue2(BRANCH_TABLE_EVM).getBranchName().equalsIgnoreCase(BRANCH_1));
        assertTrue(mapping.toValue2(BRANCH_TABLE_EVM).getServiceOutletCode().equalsIgnoreCase(CODE_1));
        assertTrue(mapping.toValue2(BRANCH_TABLE_EVM).getCounty().equalsIgnoreCase(COUNTY_1));
        assertTrue(mapping.toValue2(BRANCH_TABLE_EVM).getTown().equalsIgnoreCase(TOWN_1));
        assertThat(mapping.toValue2(BRANCH_TABLE_EVM).getYearOpened()).isEqualTo(YEAR_OPENED);
    }
}
