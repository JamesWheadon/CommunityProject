import './App.css';
import { useState } from "react";

import { Map } from '../src/components/map';
import { Directory } from './components/directory';

const fakeData = [
  {
    animal_id: 1,
    animal_name: 'Addax',
    animal_image: 'https://upload.wikimedia.org/wikipedia/commons/thumb/8/8d/A_big_male_Addax_showing_as_the_power_of_his_horns.jpg/800px-A_big_male_Addax_showing_as_the_power_of_his_horns.jpg?20160529142107',
    latitude: 31.20,
    longitude: -6.35
  },
  {
    animal_id: 2,
    animal_name: 'African_bush_elephant',
    animal_image: 'https://upload.wikimedia.org/wikipedia/commons/thumb/3/37/African_Bush_Elephant.jpg/400px-African_Bush_Elephant.jpg',
    latitude: -32.44,
    longitude: 24.74
  },
  {
    animal_id: 3,
    animal_name: 'African_wild_dog',
    animal_image: 'https://commons.wikimedia.org/wiki/File:African_wild_dog_(Lycaon_pictus_pictus).jpg',
    latitude: -18.84,
    longitude: 34.44
  },
]

function App() {
  const [hoveredOriginId, setHoveredOriginId] = useState(null)

  return (
    <>
      <header className="App-header">
        <h4>Species Reintroduction Directory</h4>
        {fakeData.map((data) => (
          <Directory
            key={data.animal_id}
            onHover={setHoveredOriginId}
            data={data} />
        ))}
      </header>
      <Map
        hoveredOriginId={hoveredOriginId} />
    </>
  );
}

export default App;
