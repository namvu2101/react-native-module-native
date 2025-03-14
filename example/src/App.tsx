import { Text, View, StyleSheet } from 'react-native';
import { useVolumeChange } from 'react-native-module-native';

export default function App() {
  const volumeChange = useVolumeChange();
  return (
    <View style={styles.container}>
      <Text>Result:{volumeChange}</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
});
