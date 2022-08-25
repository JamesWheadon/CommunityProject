import './App.css';
import { useState, useEffect } from "react";
import axios from 'axios';

import { Map } from '../src/components/map';
import { Directory } from './components/directory';

function App() {
  const [hoveredOriginId, setHoveredOriginId] = useState(null)
  const [animals, setAnimals] = useState();

  useEffect(() => {
    async function fetchAnimals() {
      const url = 'http://localhost:8080/animals'
      try {
        const options = {
          headers: {
            "Content-Type": `application/json;charset=utf-8`
          }
        };
        const { data } = await axios.get(url, options)
        if (data.err) {
          throw new Error(data.err)
        }
        setAnimals(data.animals)
      } catch {
        console.warn("There's an error!!! Cannot fetch data!")
      }
    } fetchAnimals();
  }, []);

  return (
    <>
      <header className="App-header">
        <h4>Species Reintroduction Directory</h4>
        {animals && animals.length ? animals.map((data) => (
          <Directory
            key={data.id}
            onHover={setHoveredOriginId}
            data={data} />
        )) : null}
      </header>
      <Map
        hoveredOriginId={hoveredOriginId} />
    </>
  );
}

export default App;
