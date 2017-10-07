package fil.coo.resource.pools;

import fil.coo.resource.Basket;
import fil.coo.resource.Resource;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class BasketPoolTest extends ResourcePoolTest {

	protected ResourcePool getResourcePool(int i) {
		return new BasketPool(i);
	}

	protected Resource getOneResource() {
		return resourcePool.createOneResource();
	}

	@Test
	public void testCreateOneResourcesGivesBasket() {
		assertTrue(resourcePool.createOneResource() instanceof Basket);
	}
}
