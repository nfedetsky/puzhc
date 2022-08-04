package com.softline.csrv.model.sort;

import com.softline.csrv.entity.Request;
import io.jmix.core.Sort;
import io.jmix.core.metamodel.model.MetaClass;
import io.jmix.core.metamodel.model.MetaPropertyPath;
import io.jmix.ui.model.BaseCollectionLoader;
import io.jmix.ui.model.CollectionContainer;
import io.jmix.ui.model.impl.CollectionContainerSorter;
import io.jmix.ui.model.impl.EntityValuesComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.Objects;

public class RequestCollectionContainerSorter extends CollectionContainerSorter {
    private final Logger log = LoggerFactory.getLogger(RequestCollectionContainerSorter.class.getName());

    public RequestCollectionContainerSorter(CollectionContainer container,
                                           @Nullable BaseCollectionLoader loader,
                                           BeanFactory beanFactory) {
        super(container, loader, beanFactory);
    }

    @Override
    protected Comparator<?> createComparator(Sort sort, MetaClass metaClass) {
        MetaPropertyPath metaPropertyPath = Objects.requireNonNull(
                metaClass.getPropertyPath(sort.getOrders().get(0).getProperty()));

        //log.debug(metaPropertyPath.toPathString());

        if (metaPropertyPath.getMetaClass().getJavaClass().equals(Request.class)
                && "keyNum".equals(metaPropertyPath.toPathString())) {
            boolean isAsc = sort.getOrders().get(0).getDirection() == Sort.Direction.ASC;
            return Comparator.comparing((Request e) -> e.getNumberKeyNum(),
                    new EntityValuesComparator<>(isAsc, metaClass, beanFactory));
        }
        return super.createComparator(sort, metaClass);
    }

}
