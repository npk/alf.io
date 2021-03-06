/**
 * This file is part of alf.io.
 *
 * alf.io is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * alf.io is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with alf.io.  If not, see <http://www.gnu.org/licenses/>.
 */
package alfio.controller.support;

import alfio.manager.FileUploadManager;
import alfio.model.FileBlobMetadata;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.stream.Stream;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TemplateProcessorTest {

    private final Random random = new Random(System.nanoTime());

    @Test
    public void resultingImageMustBeUnder300x150() {
        Stream.generate(() -> Pair.of(String.valueOf(random.nextInt(10_000)), String.valueOf(random.nextInt(100_000))))
            .limit(1000)
            .forEach(this::assertDimensionsUnder300x150);
    }


    @Test
    public void testCoverityFindingDivisionByZero() {
        assertDimensionsUnder300x150(Pair.of("0", "1500"));
        assertDimensionsUnder300x150(Pair.of("1500", "0"));
    }
    private void assertDimensionsUnder300x150(Pair<String, String> p) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(FileUploadManager.ATTR_IMG_WIDTH, p.getLeft());
        parameters.put(FileUploadManager.ATTR_IMG_HEIGHT, p.getRight());
        FileBlobMetadata metadata = mock(FileBlobMetadata.class);
        when(metadata.getAttributes()).thenReturn(parameters);
        Map<String,Object> model = new HashMap<>();
        TemplateProcessor.fillWithImageData(metadata, mock(FileUploadManager.class), model);
        assertTrue(Integer.parseInt(String.valueOf(model.get("imageWidth"))) <= 300);
        assertTrue(Integer.parseInt(String.valueOf(model.get("imageHeight"))) <= 150);
    }
}