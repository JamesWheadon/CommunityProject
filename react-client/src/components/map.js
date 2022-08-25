import { useMemo, useState, useEffect } from "react";
import { GoogleMap, useLoadScript, Marker, InfoWindow } from "@react-google-maps/api";
import mapStyles from "../mapStyles";
import axios from 'axios';

export const Map = ({ hoveredOriginId }) => {
  const { isLoaded } = useLoadScript({
    googleMapsApiKey: process.env.REACT_APP_GOOGLE_MAPS_API_KEY,
  });

  if (!isLoaded) return <div>Loading...</div>;
  return <FetchMap hoveredOriginId={hoveredOriginId} />;
}

const FetchMap = ({ hoveredOriginId }) => {
  const center = useMemo(() => ({ lat: 51.507351, lng: -0.127758 }), []);
  const [animals, setAnimals] = useState();
  const [selectedMarker, setSelectedMarker] = useState(null);

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

  const selectedDirectory = animals && animals.find((item) => item.id === hoveredOriginId)

  return (
    <GoogleMap zoom={10} center={center} mapContainerClassName="map-container" options={{ styles: mapStyles.styles }}>
      {animals && animals.length ? animals.map((data) => (
        <Marker
          key={data.id}
          position={{
            lat: data.latitude,
            lng: data.longitude
          }}
          onClick={() => {
            setSelectedMarker(data);
          }}
          icon={{
            url: icons[Math.floor(Math.random() * 8)],
            scaledSize: new window.google.maps.Size(75, 75)
          }}
        />
      )) : null}

      {selectedDirectory &&
        <InfoWindow
          onCloseClick={() => {
            // ????????
          }}
          position={{
            lat: selectedDirectory.latitude,
            lng: selectedDirectory.longitude
          }}
        >
          <div>
            <h1>{selectedDirectory.name.replaceAll('_', ' ')}</h1>
            <img src={selectedDirectory.image} alt={selectedDirectory.name.replaceAll('_', ' ')} />
          </div>
        </InfoWindow>
      }

      {selectedMarker && (
        <InfoWindow
          onCloseClick={() => {
            setSelectedMarker(null);
          }}
          position={{
            lat: selectedMarker.latitude,
            lng: selectedMarker.longitude
          }}
        >
          <div>
            <h1>{selectedMarker.name.replaceAll('_', ' ')}</h1>
            <img src={selectedMarker.image} alt={selectedMarker.name.replaceAll('_', ' ')} />
          </div>
        </InfoWindow>
      )}
    </GoogleMap>
  );
}