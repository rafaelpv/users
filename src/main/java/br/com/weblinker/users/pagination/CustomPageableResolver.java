package br.com.weblinker.users.pagination;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class CustomPageableResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private PaginationConfiguration paginationConfig;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return Pageable.class.equals(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {

        int page = parseInteger(webRequest.getParameter("page"), paginationConfig.getDefaultPage());
        int size = parseInteger(webRequest.getParameter("size"), paginationConfig.getDefaultSize());
        String sortParam = webRequest.getParameter("sort");

        size = Math.min(size, paginationConfig.getMaxSize());

        Sort sort = Sort.unsorted();
        if (sortParam != null && !sortParam.isEmpty()) {
            sort = parseSort(sortParam);
        }

        return PageRequest.of(page, size, sort);
    }

    private int parseInteger(String value, int defaultValue) {
        try {
            return value != null ? Integer.parseInt(value) : defaultValue;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private Sort parseSort(String sortParam) {
        String[] orders = sortParam.split(",");
        Sort.Order order = new Sort.Order(
                orders.length > 1 && "desc".equalsIgnoreCase(orders[1]) ? Sort.Direction.DESC : Sort.Direction.ASC,
                orders[0]
        );
        return Sort.by(order);
    }
}
