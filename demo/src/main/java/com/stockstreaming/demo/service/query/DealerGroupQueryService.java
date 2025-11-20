package com.stockstreaming.demo.service.query;


import com.stockstreaming.demo.model.DealerGroup;
import com.stockstreaming.demo.repository.DealerGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DealerGroupQueryService {

    private final DealerGroupRepository repository;

    public List<DealerGroup> search(List<DynamicFilter> filters) {
        Specification<DealerGroup> spec =
                new DynamicSpecificationBuilder<DealerGroup>().build(filters);

        return repository.findAll(spec);
    }
}
