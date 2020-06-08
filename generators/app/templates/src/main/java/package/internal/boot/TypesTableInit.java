package io.github.deposits.app.boot;

import io.github.deposits.repository.TypeTableRepository;
import io.github.deposits.service.TypeTableService;
import io.github.deposits.service.dto.TypeTableDTO;
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
public class TypesTableInit implements ApplicationRunner {

    private final TypeTableService typeTableService;
    private final TypeTableRepository typeTableRepository;

    public TypesTableInit(final TypeTableService typeTableService, final TypeTableRepository typeTableRepository) {
        this.typeTableService = typeTableService;
        this.typeTableRepository = typeTableRepository;
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
        if (typeTableRepository.count() == 0) {
            addedItems = getTypeTableItems().stream().peek(typeTableService::save).count();
        }

        log.info("{} items initialized in the type-table", addedItems);
    }

    private List<TypeTableDTO> getTypeTableItems() {
        List<TypeTableDTO> types = new ArrayList<>();

        types.add(TypeTableDTO.builder().glCode("10011").typeDefinition("SAVINGS").tableFDefinition("SAVINGS").dpfDefinition("SAVINGS").build());
        types.add(TypeTableDTO.builder().glCode("10012").typeDefinition("SAVINGS").tableFDefinition("SAVINGS").dpfDefinition("SAVINGS").build());
        types.add(TypeTableDTO.builder().glCode("10001").typeDefinition("CURRENT").tableFDefinition("CURRENT").dpfDefinition("DEMAND ACCOUNTS").build());
        types.add(TypeTableDTO.builder().glCode("10031").typeDefinition("CALL").tableFDefinition("TERM DEPOSITS").dpfDefinition("CALL").build());
        types.add(TypeTableDTO.builder().glCode("00042").typeDefinition("CURRENT").tableFDefinition("CURRENT").dpfDefinition("DEMAND ACCOUNTS").build());
        types.add(TypeTableDTO.builder().glCode("10021").typeDefinition("TERM DEPOSITS").tableFDefinition("TERM DEPOSITS").dpfDefinition("FIXED").build());
        types.add(TypeTableDTO.builder().glCode("10022").typeDefinition("TERM DEPOSITS").tableFDefinition("TERM DEPOSITS").dpfDefinition("FIXED").build());
        types.add(TypeTableDTO.builder().glCode("10023").typeDefinition("TERM DEPOSITS").tableFDefinition("TERM DEPOSITS").dpfDefinition("FIXED").build());
        types.add(TypeTableDTO.builder().glCode("10024").typeDefinition("TERM DEPOSITS").tableFDefinition("TERM DEPOSITS").dpfDefinition("FIXED").build());

        return types;
    }
}
