package org.aimindflow.common.excel.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Excel数据导入监听器
 *
 * @author HezaoHezao
 */
@Slf4j
public class ExcelDataListener<T> extends AnalysisEventListener<T> {

    /**
     * 数据列表
     */
    private final List<T> dataList = new ArrayList<>();

    /**
     * 批处理大小
     */
    private final int batchSize;

    /**
     * 数据处理器
     */
    private final Consumer<List<T>> dataConsumer;

    /**
     * 构造函数
     *
     * @param dataConsumer 数据处理器
     */
    public ExcelDataListener(Consumer<List<T>> dataConsumer) {
        this(1000, dataConsumer);
    }

    /**
     * 构造函数
     *
     * @param batchSize    批处理大小
     * @param dataConsumer 数据处理器
     */
    public ExcelDataListener(int batchSize, Consumer<List<T>> dataConsumer) {
        this.batchSize = batchSize;
        this.dataConsumer = dataConsumer;
    }

    @Override
    public void invoke(T data, AnalysisContext context) {
        dataList.add(data);
        // 达到批处理大小，进行处理
        if (dataList.size() >= batchSize) {
            processData();
            // 清空数据列表
            dataList.clear();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 处理剩余数据
        if (!dataList.isEmpty()) {
            processData();
        }
        log.info("Excel数据解析完成");
    }

    /**
     * 处理数据
     */
    private void processData() {
        log.info("Excel数据处理中，本批次数据量：{}", dataList.size());
        try {
            dataConsumer.accept(new ArrayList<>(dataList));
        } catch (Exception e) {
            log.error("Excel数据处理异常", e);
        }
    }
}