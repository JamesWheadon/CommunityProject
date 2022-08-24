import { useMemo, useState } from "react";
import { GoogleMap, useLoadScript, Marker, InfoWindow } from "@react-google-maps/api";
import mapStyles from "../mapStyles";  

export const Map = ({hoveredOriginId}) => {
  const { isLoaded } = useLoadScript({
    googleMapsApiKey: process.env.REACT_APP_GOOGLE_MAPS_API_KEY,
  });

  if (!isLoaded) return <div>Loading...</div>;
  return <FetchMap hoveredOriginId={hoveredOriginId} />;
}

const FetchMap = ({hoveredOriginId, data}) => {

  console.log(hoveredOriginId);
  const center = useMemo(() => ({ lat: 51.507351, lng: -0.127758 }), []);
  const [selectedItem, setSelectedItem] = useState(null);
  
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

  const selectedOrigin = fakeData.find(({ item }) => selectedItem === hoveredOriginId)
  console.log(selectedOrigin)

  return (
    <GoogleMap zoom={10} center={center} mapContainerClassName="map-container" options={{ styles: mapStyles.styles }}>
      {fakeData.map(data => (
        <Marker 
          key={data.animal_id}
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
      ))}

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
            <h1>{selectedItem.animal_name.replaceAll('_', ' ')}</h1>
            <img src={selectedItem.animal_image} alt={selectedItem.animal_name.replaceAll('_', ' ')} />
          </div>
        </InfoWindow>
      )}
    </GoogleMap>
  );
}