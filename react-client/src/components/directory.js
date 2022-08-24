import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import ListSubheader from '@mui/material/ListSubheader';

export const Directory = ({ id, data, onHover }) => {

  const handleClick = () => {
    onHover(data.id)
  }

  return (
    <>
      <List
        sx={{
          width: '100%',
          position: 'relative',
          maxHeight: 600,
          '& ul': { padding: 0 },
        }}
        subheader={<li />}
      >
        {data && (
          <li key={data.id}>
            <ul>
              <ListSubheader>{data.name.replaceAll('_', ' ')}</ListSubheader>
              <ListItem onClick={handleClick} key={data.animal_id}>
                <img src={data.image} alt={data.name.replaceAll('_', ' ')} height="200px" width="200px" />
              </ListItem>
            </ul>
          </li>
        )}
      </List>
    </>
  );
}