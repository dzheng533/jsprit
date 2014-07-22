/*******************************************************************************
 * Copyright (c) 2014 Stefan Schroeder.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either 
 * version 3.0 of the License, or (at your option) any later version.
 *  
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this library.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Stefan Schroeder - initial API and implementation
 ******************************************************************************/
package jsprit.core.algorithm.state;

import jsprit.core.problem.Capacity;
import jsprit.core.problem.VehicleRoutingProblem;
import jsprit.core.problem.job.Service;
import jsprit.core.problem.solution.route.VehicleRoute;
import jsprit.core.problem.solution.route.activity.TourActivity;
import jsprit.core.problem.solution.route.state.StateFactory;
import jsprit.core.problem.solution.route.state.StateFactory.StateId;
import jsprit.core.problem.vehicle.Vehicle;
import jsprit.core.problem.vehicle.VehicleImpl;
import jsprit.core.problem.vehicle.VehicleType;
import jsprit.core.problem.vehicle.VehicleTypeImpl;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class StateManagerTest {
	

    private VehicleRoute getRoute(Vehicle vehicle) {
        return VehicleRoute.Builder.newInstance(vehicle).addService(Service.Builder.newInstance("s").setLocationId("loc").build()).build();
    }

    @Test
	public void whenRouteStateIsSetWithGenericMethodAndBoolean_itMustBeSetCorrectly(){
		VehicleRoute route = getRoute(mock(Vehicle.class));
		StateManager stateManager = new StateManager(mock(VehicleRoutingProblem.class));
		StateId id = stateManager.createStateId("myState");
		stateManager.putRouteState(route, id, true);
		assertTrue(stateManager.getRouteState(route, id, Boolean.class));
	}
	
	@Test
	public void whenRouteStateIsSetWithGenericMethodAndInteger_itMustBeSetCorrectly(){
		VehicleRoute route = getRoute(mock(Vehicle.class));
		StateManager stateManager = new StateManager(mock(VehicleRoutingProblem.class));
		StateId id = stateManager.createStateId("myState");
		int load = 3;
		stateManager.putRouteState(route, id, load);
		int getLoad = stateManager.getRouteState(route, id, Integer.class);
		assertEquals(3, getLoad);
	}
	
	@Test
	public void whenRouteStateIsSetWithGenericMethodAndCapacity_itMustBeSetCorrectly(){
		VehicleRoute route = getRoute(mock(Vehicle.class));
		StateManager stateManager = new StateManager(mock(VehicleRoutingProblem.class));
		StateId id = stateManager.createStateId("myState");
		Capacity capacity = Capacity.Builder.newInstance().addDimension(0, 500).build();
		stateManager.putRouteState(route, id, capacity);
		Capacity getCap = stateManager.getRouteState(route, id, Capacity.class);
		assertEquals(500, getCap.get(0));
	}
	

	
	@Test
	public void whenActivityStateIsSetWithGenericMethodAndBoolean_itMustBeSetCorrectly(){
		TourActivity activity = mock(TourActivity.class);
		StateManager stateManager = new StateManager(mock(VehicleRoutingProblem.class));
		StateId id = stateManager.createStateId("myState");
		stateManager.putActivityState(activity, id, true);
		assertTrue(stateManager.getActivityState(activity, id, Boolean.class));
	}
	
	@Test
	public void whenActivityStateIsSetWithGenericMethodAndInteger_itMustBeSetCorrectly(){
		TourActivity activity = mock(TourActivity.class);
		StateManager stateManager = new StateManager(mock(VehicleRoutingProblem.class));
		StateId id = stateManager.createStateId("myState");
		int load = 3;
		stateManager.putActivityState(activity, id, load);
		int getLoad = stateManager.getActivityState(activity, id, Integer.class);
		assertEquals(3, getLoad);
	}
	
	@Test
	public void whenActivityStateIsSetWithGenericMethodAndCapacity_itMustBeSetCorrectly(){
		TourActivity activity = mock(TourActivity.class);
		StateManager stateManager = new StateManager(mock(VehicleRoutingProblem.class));
		StateId id = stateManager.createStateId("myState");
		Capacity capacity = Capacity.Builder.newInstance().addDimension(0, 500).build();
		stateManager.putActivityState(activity, id, capacity);
		Capacity getCap = stateManager.getActivityState(activity, id, Capacity.class);
		assertEquals(500, getCap.get(0));
	}
	
	@Test
	public void whenProblemStateIsSet_itMustBeSetCorrectly(){
		StateManager stateManager = new StateManager(mock(VehicleRoutingProblem.class));
		StateId id = stateManager.createStateId("problemState");
		stateManager.putProblemState(id, Boolean.class, true);
		boolean problemState = stateManager.getProblemState(id, Boolean.class);
		assertTrue(problemState);
	}
	
	@Test(expected=NullPointerException.class)
	public void whenProblemStateIsSetAndStateManagerClearedAfterwards_itThrowsException(){
		StateManager stateManager = new StateManager(mock(VehicleRoutingProblem.class));
		StateId id = stateManager.createStateId("problemState");
		stateManager.putProblemState(id, Boolean.class, true);
		stateManager.clear();
		@SuppressWarnings("unused")
		boolean problemState = stateManager.getProblemState(id, Boolean.class);
	}
	
	@Test
	public void whenProblemStateIsSetAndStateManagerClearedAfterwards_itReturnsNull(){
		StateManager stateManager = new StateManager(mock(VehicleRoutingProblem.class));
		StateId id = StateFactory.createId("problemState");
		stateManager.addDefaultProblemState(id, Boolean.class, false);
		stateManager.putProblemState(id, Boolean.class, true);
		stateManager.clear();
		Boolean problemState = stateManager.getProblemState(id, Boolean.class);
		assertNull(problemState);
	}

    @Test
    public void whenCreatingNewState_itShouldHaveAnIndex(){
        StateManager stateManager = new StateManager(mock(VehicleRoutingProblem.class));
        StateId stateId = stateManager.createStateId("foo-state");
        assertEquals(10,stateId.getIndex());
    }

    @Test
    public void whenCreatingNewStates_theyShouldHaveAnIndex(){
        StateManager stateManager = new StateManager(mock(VehicleRoutingProblem.class));
        StateId fooState = stateManager.createStateId("foo-state");
        StateId foofooState = stateManager.createStateId("foo-foo-state");
        assertEquals(10,fooState.getIndex());
        assertEquals(11,foofooState.getIndex());
    }

    @Test
    public void whenCreatingTwoStatesWithTheSameName_theyShouldHaveTheSameIndex(){
        StateManager stateManager = new StateManager(mock(VehicleRoutingProblem.class));
        StateId fooState = stateManager.createStateId("foo-state");
        StateId foofooState = stateManager.createStateId("foo-state");
        assertEquals(10, fooState.getIndex());
        assertEquals(10, foofooState.getIndex());
    }

    @Test
    public void whenCreatingAVehicleDependentRouteState_itShouldBeMemorized(){
        Vehicle vehicle = VehicleImpl.Builder.newInstance("v").setStartLocationId("loc").build();
        VehicleRoute route = getRoute(vehicle);
        StateManager stateManager = new StateManager(mock(VehicleRoutingProblem.class));
        StateId id = stateManager.createStateId("myState");
        Capacity capacity = Capacity.Builder.newInstance().addDimension(0, 500).build();
        stateManager.putRouteState(route, vehicle, id, capacity);
        Capacity getCap = stateManager.getRouteState(route, vehicle, id, Capacity.class);
        assertEquals(500, getCap.get(0));
    }

    @Test
    public void whenCreatingAVehicleDependentActivityState_itShouldBeMemorized(){
        Vehicle vehicle = VehicleImpl.Builder.newInstance("v").setStartLocationId("loc").build();
        StateManager stateManager = new StateManager(mock(VehicleRoutingProblem.class));
        StateId id = stateManager.createStateId("myState");
        Capacity capacity = Capacity.Builder.newInstance().addDimension(0, 500).build();
        TourActivity act = mock(TourActivity.class);
        stateManager.putActivityState(act, vehicle, id, capacity);
        Capacity getCap = stateManager.getActivityState(act, vehicle, id, Capacity.class);
        assertEquals(500, getCap.get(0));
    }

    @Test
    public void whenMemorizingVehicleInfo_itShouldBeMemorized(){
        Vehicle vehicle = VehicleImpl.Builder.newInstance("v").setStartLocationId("loc").build();
        VehicleRoute route = getRoute(vehicle);
        StateManager stateManager = new StateManager(mock(VehicleRoutingProblem.class));
        StateId id = stateManager.createStateId("vehicleParam");
        double distanceParam = vehicle.getType().getVehicleCostParams().perDistanceUnit;
        stateManager.putRouteState(route,vehicle, id,distanceParam);
        assertEquals(1.,stateManager.getRouteState(route,vehicle,id, Double.class),0.01);
    }

    @Test
    public void whenMemorizingTwoVehicleInfoForRoute_itShouldBeMemorized(){
        VehicleType type = VehicleTypeImpl.Builder.newInstance("t").setCostPerDistance(4.).build();
        VehicleImpl vehicle = VehicleImpl.Builder.newInstance("v").setStartLocationId("loc").build();
        VehicleImpl vehicle2 = VehicleImpl.Builder.newInstance("v").setStartLocationId("loc").setType(type).build();
        VehicleRoute route = getRoute(vehicle);

        //getting the indices created in vrpBuilder
        VehicleRoutingProblem.Builder vrpBuilder = VehicleRoutingProblem.Builder.newInstance();
        VehicleRoutingProblem vrp = vrpBuilder.addVehicle(vehicle).addVehicle(vehicle2).build();

        StateManager stateManager = new StateManager(vrp);
        StateId id = stateManager.createStateId("vehicleParam");
        double distanceParam = vehicle.getType().getVehicleCostParams().perDistanceUnit;
        stateManager.putRouteState(route,vehicle, id,distanceParam);
        stateManager.putRouteState(route,vehicle2,id,vehicle2.getType().getVehicleCostParams().perDistanceUnit);
        assertEquals(1., stateManager.getRouteState(route, vehicle, id, Double.class), 0.01);
        assertEquals(4.,stateManager.getRouteState(route,vehicle2,id, Double.class),0.01);
    }

    @Test
    public void whenMemorizingTwoVehicleInfoForAct_itShouldBeMemorized(){
        VehicleType type = VehicleTypeImpl.Builder.newInstance("t").setCostPerDistance(4.).build();
        VehicleImpl vehicle = VehicleImpl.Builder.newInstance("v").setStartLocationId("loc").build();
        VehicleImpl vehicle2 = VehicleImpl.Builder.newInstance("v").setStartLocationId("loc").setType(type).build();

        //getting the indices created in vrpBuilder
        VehicleRoutingProblem.Builder vrpBuilder = VehicleRoutingProblem.Builder.newInstance();
        VehicleRoutingProblem vrp = vrpBuilder.addVehicle(vehicle).addVehicle(vehicle2).build();

        TourActivity act = mock(TourActivity.class);

        StateManager stateManager = new StateManager(vrp);
        StateId id = stateManager.createStateId("vehicleParam");
        double distanceParam = vehicle.getType().getVehicleCostParams().perDistanceUnit;
        stateManager.putActivityState(act, vehicle, id, distanceParam);
        stateManager.putActivityState(act, vehicle2, id, vehicle2.getType().getVehicleCostParams().perDistanceUnit);

        assertEquals(1., stateManager.getActivityState(act, vehicle, id, Double.class), 0.01);
        assertEquals(4.,stateManager.getActivityState(act,vehicle2, id, Double.class),0.01);
    }

    @Test
    public void whenClearing_arrElementsShouldBeNull(){
        VehicleType type = VehicleTypeImpl.Builder.newInstance("t").setCostPerDistance(4.).build();
        VehicleImpl vehicle = VehicleImpl.Builder.newInstance("v").setStartLocationId("loc").build();
        VehicleImpl vehicle2 = VehicleImpl.Builder.newInstance("v").setStartLocationId("loc").setType(type).build();

        //getting the indices created in vrpBuilder
        VehicleRoutingProblem.Builder vrpBuilder = VehicleRoutingProblem.Builder.newInstance();
        VehicleRoutingProblem vrp = vrpBuilder.addVehicle(vehicle).addVehicle(vehicle2).build();

        TourActivity act = mock(TourActivity.class);

        StateManager stateManager = new StateManager(vrp);
        StateId id = stateManager.createStateId("vehicleParam");
        double distanceParam = vehicle.getType().getVehicleCostParams().perDistanceUnit;
        stateManager.putActivityState(act, vehicle, id, distanceParam);
        stateManager.putActivityState(act, vehicle2, id, vehicle2.getType().getVehicleCostParams().perDistanceUnit);

        stateManager.clear();

        assertNull(stateManager.getActivityState(act, vehicle, id, Double.class));
        assertNull(stateManager.getActivityState(act,vehicle2, id, Double.class));
    }
}
