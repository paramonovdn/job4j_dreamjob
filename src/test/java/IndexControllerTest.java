import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.dreamjob.controller.IndexController;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class IndexControllerTest {

    private IndexController indexController;

    @BeforeEach
    public void initServices() {
        indexController = new IndexController();
    }

    @Test
    public void test() {
        var model = new ConcurrentModel();
        var view = indexController.getIndex();

        assertThat(view).isEqualTo("index");
    }
}
