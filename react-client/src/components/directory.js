import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import ListItemText from '@mui/material/ListItemText';
import ListSubheader from '@mui/material/ListSubheader';

export const Directory = ({ id, data, onHover }) => {

  const handleClick = () => {
    onHover(data.animal_id)
  }

  return (
    <>
      <List
        sx={{
          width: '100%',
          // maxWidth: 360,
          position: 'relative',
          maxHeight: 600,
          '& ul': { padding: 0 },
        }}
        subheader={<li />}
      >
        {data && (
          <li key={data.animal_id}>
            <ul>
              <ListSubheader>{data.animal_name.replaceAll('_', ' ')}</ListSubheader>
              <ListItem onClick={handleClick} key={data.animal_id}>
                <img src={data.animal_image} alt={data.animal_name.replaceAll('_', ' ')} height="200px" width="200px" />
              </ListItem>
            </ul>
          </li>
        )}
      </List>
    </>
  );
}