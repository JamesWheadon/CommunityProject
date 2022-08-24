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

  // console.log(hoveredOriginId);
  const center = useMemo(() => ({ lat: 51.507351, lng: -0.127758 }), []);
  const [animals, setAnimals] = useState();
  const [selectedItem, setSelectedItem] = useState(null);

  console.log(selectedItem)

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

  // const selectedOrigin = data.find(({ item }) => selectedItem === hoveredOriginId)
  // console.log(selectedOrigin)

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
            setSelectedItem(data);
          }}
          icon={{
            url: 'https://cdn.shopify.com/s/files/1/1061/1924/products/Pig_Emoji_large.png?v=1571606065',
            scaledSize: new window.google.maps.Size(75, 75)
          }}
        />
      )) : null}

      {selectedItem && (
        <InfoWindow
          onCloseClick={() => {
            setSelectedItem(null);
          }}
          position={{
            lat: selectedItem.latitude,
            lng: selectedItem.longitude
          }}
        >
          <div>
            <h1>{selectedItem.name.replaceAll('_', ' ')}</h1>
            <img src={selectedItem.image} alt={selectedItem.name.replaceAll('_', ' ')} />
          </div>
        </InfoWindow>
      )}
    </GoogleMap>
  );
}