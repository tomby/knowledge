package org.support.project.knowledge.control.api.internal.stocks;

import java.lang.invoke.MethodHandles;

import org.support.project.common.log.Log;
import org.support.project.common.log.LogFactory;
import org.support.project.common.util.StringUtils;
import org.support.project.di.DI;
import org.support.project.di.Instance;
import org.support.project.knowledge.dao.StocksDao;
import org.support.project.knowledge.entity.StocksEntity;
import org.support.project.web.boundary.Boundary;
import org.support.project.web.common.HttpStatus;
import org.support.project.web.control.ApiControl;
import org.support.project.web.control.service.Get;

@DI(instance = Instance.Prototype)
public class GetStockOneApiControl extends ApiControl {
    /** ログ */
    private static final Log LOG = LogFactory.getLog(MethodHandles.lookup());
    /**
     * ストックを取得
     * @throws Exception 
     */
    @Get(path="_api/stocks/:id")
    public Boundary execute() throws Exception {
        LOG.trace("_api/stocks/:id");
        String id = super.getParam("id");
        LOG.debug(id);
        if (!StringUtils.isLong(id)) {
            return sendError(HttpStatus.SC_400_BAD_REQUEST);
        }
        Long stockId = new Long(id);
        StocksEntity stocksEntity = StocksDao.get().selectOnKey(stockId);
        if (stocksEntity == null) {
            return sendError(HttpStatus.SC_404_NOT_FOUND);
        }
        if (stocksEntity.getInsertUser().equals(getLoginUserId())) {
            return sendError(HttpStatus.SC_403_FORBIDDEN);
        }
        return send(HttpStatus.SC_200_OK, stocksEntity);
    }
}
