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

  const icons = [
    'https://cdn.shopify.com/s/files/1/1061/1924/products/Horse_emoji_icon_png_large.png?v=1571606088',
    'https://cdn.shopify.com/s/files/1/1061/1924/products/Pig_Emoji_large.png?v=1571606065',
    'https://cdn.shopify.com/s/files/1/1061/1924/products/Dog_Emoji_large.png?v=1571606065',
    'https://cdn.shopify.com/s/files/1/1061/1924/products/CAT_emoji_icon_png_large.png?v=1571606068',
    'https://cdn.shopify.com/s/files/1/1061/1924/products/Grey_Bird_Emoji_PNG_large.png?v=1571606088',
    'https://cdn.shopify.com/s/files/1/1061/1924/products/chicken_emoji_icon_png_large.png?v=1571606068',
    'https://cdn.shopify.com/s/files/1/1061/1924/products/tiger_emoji_icon_png_large.png?v=1571606089',
    'https://cdn.shopify.com/s/files/1/1061/1924/products/Monkey_Face_Emoji_large.png?v=1571606065'
  ]

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
            scaledSize: new window.google.maps.Size(50, 50)
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
            <img src={selectedMarker.image} alt={selectedMarker.name.replaceAll('_', ' ')} />
            <h3 style={{ textAlign: 'center'}}>{selectedMarker.name.replaceAll('_', ' ')}</h3>
          </div>
        </InfoWindow>
      )}
    </GoogleMap>
  );
}