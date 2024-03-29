package it.polimi.ingsw.model.map;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MapLoaderTest {

        //
        //test that the function for hashmap work well, and that the other variable are read correctly
        //

        @Test
        void mapLoadedContentCheck(){

            List<Square> squares = MapLoader.loadMap();
            assertEquals(25, squares.size());
            assert (squares.get(3).getCanAccess().get(Directions.OVEST) == 3);
            assertNotEquals(true, squares.get(6).hasPlayer());
            assertEquals(0, squares.get(15).getBuildingLevel());
            assertEquals(Building.GROUND,squares.get(24).getBuilding());
            assertEquals(1, (int) squares.get(0).getTile());
            assertEquals(17, (int) squares.get(16).getTile());
            assertEquals(3,squares.get(7).getCoordinates()[0]);
            assertEquals(4,squares.get(7).getCoordinates()[1]);



        }





    }