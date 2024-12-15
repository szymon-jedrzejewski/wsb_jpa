package com.jpacourse.mapper;

import com.jpacourse.dto.VisitTO;
import com.jpacourse.persistence.entity.VisitEntity;

import java.util.List;
import java.util.stream.Collectors;

public final class VisitMapper {

    public static VisitTO mapToTO(final VisitEntity visitEntity) {
        if (visitEntity == null) {
            return null;
        }

        VisitTO visitTO = new VisitTO();
        visitTO.setId(visitEntity.getId());
        visitTO.setDescription(visitEntity.getDescription());
        visitTO.setTime(visitEntity.getTime());
        return visitTO;
    }

    public static List<VisitTO> mapToTOList(List<VisitEntity> visitEntities) {
        if (visitEntities == null) {
            return null;
        }
        return visitEntities.stream()
                .map(VisitMapper::mapToTO)
                .collect(Collectors.toList());
    }

    public static VisitEntity mapToEntity(final VisitTO visitTO) {
        if (visitTO == null) {
            return null;
        }
        VisitEntity visitEntity = new VisitEntity();
        visitEntity.setId(visitTO.getId());
        visitEntity.setDescription(visitTO.getDescription());
        visitEntity.setTime(visitTO.getTime());
        return visitEntity;
    }

    public static List<VisitEntity> mapToEntityList(List<VisitTO> visitTOs) {
        if (visitTOs == null) {
            return null;
        }
        return visitTOs.stream()
                .map(VisitMapper::mapToEntity)
                .collect(Collectors.toList());
    }
}
