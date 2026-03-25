import { Stack } from 'expo-router';

export default function RootLayout() {
  return <Stack screenOptions={{ headerTitleStyle: { fontSize: 20 }, headerBackTitleVisible: false }} />;
}
