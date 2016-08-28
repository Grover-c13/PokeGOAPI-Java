package com.pokegoapi.decode;

import com.annimon.stream.Stream;
import com.annimon.stream.function.Consumer;
import com.annimon.stream.function.Function;
import com.pokegoapi.api.map.Map;
import com.pokegoapi.google.common.geometry.S2Cell;
import com.pokegoapi.google.common.geometry.S2CellId;
import com.pokegoapi.google.common.geometry.S2LatLng;
import com.pokegoapi.google.common.geometry.S2Point;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by paul on 27-8-2016.
 */
@Slf4j
public class MapCoords {
	private final String app1 = "CellId: 5171985602291171328\n" +
			"CellId: 5171985630208458752\n" +
			"CellId: 5171985632355942400\n" +
			"CellId: 5171985634503426048\n" +
			"CellId: 5171985640945876992\n" +
			"CellId: 5172030284983435264\n" +
			"CellId: 5172030291425886208\n" +
			"CellId: 5172030293573369856\n" +
			"CellId: 5172030295720853504\n" +
			"CellId: 5172030297868337152\n" +
			"CellId: 5172030300015820800\n" +
			"CellId: 5172030302163304448\n" +
			"CellId: 5172030304310788096\n" +
			"CellId: 5172030306458271744\n" +
			"CellId: 5172030308605755392\n" +
			"CellId: 5172030310753239040\n" +
			"CellId: 5172030317195689984\n" +
			"CellId: 5172030330080591872\n" +
			"CellId: 5172030332228075520";

	private final String app2 = "CellId: 5171985602291171328\n" +
			"CellId: 5171985630208458752\n" +
			"CellId: 5171985632355942400\n" +
			"CellId: 5171985634503426048\n" +
			"CellId: 5171985640945876992\n" +
			"CellId: 5172030284983435264\n" +
			"CellId: 5172030291425886208\n" +
			"CellId: 5172030293573369856\n" +
			"CellId: 5172030295720853504\n" +
			"CellId: 5172030297868337152\n" +
			"CellId: 5172030300015820800\n" +
			"CellId: 5172030302163304448\n" +
			"CellId: 5172030304310788096\n" +
			"CellId: 5172030306458271744\n" +
			"CellId: 5172030308605755392\n" +
			"CellId: 5172030310753239040\n" +
			"CellId: 5172030317195689984\n" +
			"CellId: 5172030330080591872\n" +
			"CellId: 5172030332228075520\n";

	private static List<Long> parse(String str) {
		List<Long> result = new LinkedList<>();
		for (String line : str.split("\n")) {
			result.add(Long.valueOf(line.substring(8)));
		}
		return result;
	}

	@Test
	public void match1() {
		double lat = 51.5717353821;
		double lng = 5.08120012283;
		List<Long> cellsGenerated = Map.getCellIds(lat, lng);
		List<Long> cellsApp = parse(app1);
		System.out.println("App: ");
		plot(cellsApp);
		System.out.println("Generated: ");
		plot(cellsGenerated);
		assertEquals(cellsApp.size(), cellsGenerated.size());
		for (int i=0;i!=cellsApp.size();i++) {
			assertEquals("Mismatch cell " + i, cellsApp.get(i), cellsGenerated.get(i));
		}
	}


	@Test
	public void match2() {
		double lat = 51.571762085;
		double lng = 5.08113193512;
		List<Long> cellsGenerated = Map.getCellIds(lat, lng);
		List<Long> cellsApp = parse(app2);
		System.out.println("App: ");
		plot(cellsApp);
		System.out.println("Generated: ");
		plot(cellsGenerated);
		assertEquals(cellsApp.size(), cellsGenerated.size());
		for (int i=0;i!=cellsApp.size();i++) {
			assertEquals("Mismatch cell " + i, cellsApp.get(i), cellsGenerated.get(i));
		}
	}

	static void plot(List<Long> cellIds) {
		Stream.of(cellIds).map(new Function<Long, S2CellId>() {
			@Override
			public S2CellId apply(Long aLong) {
				return new S2CellId(aLong);
			}
		}).map(new Function<S2CellId, S2LatLng>() {
			@Override
			public S2LatLng apply(S2CellId s2CellId) {
				return s2CellId.toLatLng();
			}
		}).forEach(new Consumer<S2LatLng>() {
			@Override
			public void accept(S2LatLng s2LatLng) {
				System.out.println(s2LatLng.lat().degrees() + "," + s2LatLng.lng().degrees());
			}
		});
	}
}
