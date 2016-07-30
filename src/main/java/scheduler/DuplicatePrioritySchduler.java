/**
 * 
 */
package scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.scheduler.Scheduler;
import us.codecraft.webmagic.scheduler.component.DuplicateRemover;
import us.codecraft.webmagic.scheduler.component.HashSetDuplicateRemover;

/**
 *************************
 * 
 ************************* 
 * @author kootain
 * @creation 2016年7月30日
 *
 */
public abstract class DuplicatePrioritySchduler implements Scheduler{
	protected Logger logger = LoggerFactory.getLogger(getClass());

    private DuplicateRemover duplicatedRemover = new HashSetDuplicateRemover();

    public DuplicateRemover getDuplicateRemover() {
        return duplicatedRemover;
    }

    public DuplicatePrioritySchduler setDuplicateRemover(DuplicateRemover duplicatedRemover) {
        this.duplicatedRemover = duplicatedRemover;
        return this;
    }

    @Override
    public void push(Request request, Task task) {
        logger.trace("get a candidate url {}", request.getUrl());
        if (!duplicatedRemover.isDuplicate(request, task) || shouldReserved(request)) {
            logger.debug("push to queue {}", request.getUrl());
            pushWhenNoDuplicate(request, task);
        }
    }

    protected boolean shouldReserved(Request request) {
        return request.getExtra(Request.CYCLE_TRIED_TIMES) != null;
    }

    protected void pushWhenNoDuplicate(Request request, Task task) {

    }
}
