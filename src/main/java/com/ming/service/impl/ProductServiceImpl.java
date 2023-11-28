package com.ming.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ming.entities.Product;
import com.ming.exception.ServiceException;
import com.ming.service.ProductService;
import com.ming.mapper.ProductMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Y
 * @description 针对表【product】的数据库操作Service实现
 * @createDate 2023-11-28 15:30:19
 */
@Slf4j
@Service
@AllArgsConstructor
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product>
        implements ProductService {

    private final ProductMapper productMapper;

    @Override
    public Product findByIdProduct(String id) {
        return productMapper.findByIdProduct(id);
    }

    /**
     * 上面的测试已经表明了平常的接口如果不做并发量的处理后果可想而知，转化一下就可以发现本质是线程安全问题，有人说线程安全？
     * 在直接在方法上加上synchronized就好了吗？是的没错，我已经测试了加上这玩意结果也是正确了，但是你们知道这个玩意也是悲观锁吗
     * 好了对比两锁的吞吐量你就会发现乐观锁效率比悲观锁要高，而且逻辑性越强，复杂度越高，越明显，因为悲观锁是串行的。
     * 但是乐观锁虽说效率高但是更新率很低，可以看到上面1000个并发执行的时候才更新成功198次，可以说这两种锁是各自弥补缺点，什么为什么不一起用？我只能说一起用效率是最低的，虽然各自弥补缺点，但并不是相辅相成。
     * 这就是为什么读为居多用乐观，写为居多用悲观了
     *
     * @param product
     * @return
     */
    @Transactional(rollbackFor = ServiceException.class)
    @Override
    public int updateByIdProduct(Product product) {
        int rows = 0;
        try {
            rows = productMapper.updateByIdProduct(product);
        } catch (Exception e) {
            log.error("扣减失败", e);
            throw new ServiceException("扣减失败");
        }
        return rows;
    }

}




